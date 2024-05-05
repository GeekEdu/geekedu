package com.zch.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.path.LearnPathVO;
import com.zch.api.vo.trade.order.*;
import com.zch.api.vo.trade.pay.PayInfoVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.api.vo.user.VipVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import com.zch.trade.domain.dto.OrderCountDTO;
import com.zch.trade.domain.dto.PayCountDTO;
import com.zch.trade.domain.po.Order;
import com.zch.trade.enums.OrderStatusEnum;
import com.zch.trade.enums.PayStatusEnum;
import com.zch.trade.enums.PayTypeEnum;
import com.zch.trade.enums.ProductTypeEnum;
import com.zch.trade.mapper.OrderMapper;
import com.zch.trade.service.IOrderService;
import com.zch.trade.utils.IDWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zch.trade.enums.ProductTypeEnum.*;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final UserFeignClient userFeignClient;

    private final TradeFeignClient tradeFeignClient;

    private final BookFeignClient bookFeignClient;

    private final CourseFeignClient courseFeignClient;

    private final RocketMQTemplate rocketMQTemplate;

    private final OrderMapper orderMapper;

    @Override
    public OrderVO createOrder(CreateOrderForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getGoodsId()) || StringUtils.isEmpty(form.getGoodsType())
                || ObjectUtils.isNull(ProductTypeEnum.valueOf(form.getGoodsType()))) {
            return null;
        }
        OrderVO vo = new OrderVO();
        // 查找对应商品的价格
        if (Objects.equals(VIP, ProductTypeEnum.valueOf(form.getGoodsType()))) {
            // vip 会员
            Response<BigDecimal> res = userFeignClient.getVipPriceById(form.getGoodsId());
            if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                vo.setGoodsPrice(res.getData());
            }
            vo.setGoodsType(VIP.getValue());
        } else if (Objects.equals(REPLAY_COURSE, ProductTypeEnum.valueOf(form.getGoodsType()))) {
            // 录播课
            Response<BigDecimal> res = courseFeignClient.queryCoursePrice(form.getGoodsId());
            if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                vo.setGoodsPrice(res.getData());
            }
            vo.setGoodsType(REPLAY_COURSE.getValue());
        }
        // 用户id
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        vo.setUserId(userId);
        // 商品名
        vo.setGoodsName(form.getGoodsName());
        // 订单备注
        vo.setOrderNotes(form.getGoodsType());
        // 查找优惠券 TODO
        vo.setGoodsDiscount(BigDecimal.ZERO);
        // 真实价格 = 原价 - 优惠价格
        vo.setAmount(vo.getGoodsPrice().subtract(vo.getGoodsDiscount()));
        // 支付方式
        vo.setPayType(ObjectUtils.isNull(PayTypeEnum.valueOf(form.getPayment())) ? "支付宝" : PayTypeEnum.valueOf(form.getPayment()).getValue());
        // 订单创建时间
        LocalDateTime now = LocalDateTime.now();
        vo.setCreatedTime(now);
        // 订单结束时间 为当前时间加上 15 分钟
        vo.setCancelTime(now.plusMinutes(15));
        // 订单状态设置为 已下单 未支付
        vo.setOrderStatusText(OrderStatusEnum.ORDERED_NO_PAY.getValue());
        // 订单编号 使用 IDWorker 设置
        String orderId = generateOrderId(now);
        vo.setOrderId(orderId);
        // 通知消息队列
        // 写入支付信息
        PayInfoForm form1 = new PayInfoForm();
        form1.setPayName(vo.getGoodsName());
        form1.setOrderId(orderId);
        form1.setPayChannel(form.getPayment());
        form1.setPayAmount(vo.getAmount());
        form1.setOrderId(vo.getOrderId());
        Message<PayInfoForm> payInfoMsg = MessageBuilder.withPayload(form1).build();
        rocketMQTemplate.asyncSend("payInfo-2Mysql-topic", payInfoMsg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.debug("写入支付信息-消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("写入支付信息-消息发送失败");
            }
        });
        tradeFeignClient.createPayInfo(form1);
        // 存入数据库
        handle2MySQL(form, vo);
        return vo;
    }

    @Override
    public Page<OrderFullVO> getOrderPage(Long userId, Integer pageNum, Integer pageSize) {
        Page<OrderFullVO> vo = new Page<>();
        Page<Order> page = page(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            OrderFullVO vo1 = new OrderFullVO();
            vo1.setOrderId(item.getOrderId());
            vo1.setUserId(userId);
            vo1.setAmount(item.getAmount());
            vo1.setOrderNumber(item.getOrderNumber());
            vo1.setPayment(item.getPayType().getValue());
            vo1.setPayTypeText(item.getPayType().getValue());
            vo1.setOrderStatusText(item.getOrderStatus().getValue());
            vo1.setCreatedTime(item.getCreatedTime());
            vo1.setIsRefund(item.getOrderStatus().equals(OrderStatusEnum.ORDERED_AND_REFUNDED));
            // 判断商品类型，查找对应的商品信息
            if (VIP.equals(item.getGoodsType())) {
                VipVO data = userFeignClient.getVipById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            } else if (REPLAY_COURSE.equals(item.getGoodsType())) {
                CourseVO data = courseFeignClient.getCourseById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                goods.setGoodsCover(data.getCoverLink());
                vo1.setGoods(goods);
            } else if (LIVE_COURSE.equals(item.getGoodsType())) {
                LiveCourseVO data = courseFeignClient.getLiveCourseDetail(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                vo1.setGoods(goods);
            } else if (IMAGE_TEXT.equals(item.getGoodsType())) {
                ImageTextVO data = bookFeignClient.getImageTextById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                vo1.setGoods(goods);
            } else if (LEARN_PATH.equals(item.getGoodsType())) {
                LearnPathVO data = bookFeignClient.getPathDetail(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            } else if (E_BOOK.equals(item.getGoodsType())) {
                EBookVO data = bookFeignClient.getEBookById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            }
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(count());
        return vo;
    }

    @Override
    public Boolean queryOrderIsPay(Long userId, Integer goodsId, String goodsType) {
        Order one = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(Order::getGoodsId, goodsId)
                .eq(Order::getGoodsType, ProductTypeEnum.valueOf(goodsType)));
        if (ObjectUtils.isNotNull(one)) {
            // 查询支付信息表
            Response<PayInfoVO> res = tradeFeignClient.queryPayInfo(one.getOrderNumber());
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                return false;
            }
            return one.getOrderStatus().equals(OrderStatusEnum.ORDERED_AND_PAID) && res.getData().getIsPaid();
        }
        return false;
    }

    @Override
    public Boolean updateOrderStatus(String orderNumber) {
        Order order = getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNumber, orderNumber));
        if (ObjectUtils.isNotNull(order)) {
            order.setOrderStatus(OrderStatusEnum.ORDERED_AND_PAID);
            order.setPayStatus(PayStatusEnum.HAVE_PAID);
            updateById(order);
            return true;
        }
        return false;
    }

    @Override
    public OrderVO queryOrderByGoods(Integer goodsId, String goodsType, Long userId, Boolean isSeckill, LocalDateTime startAt, LocalDateTime endAt) {
        ProductTypeEnum type = REPLAY_COURSE;
        switch (goodsType) {
            case "course" -> type = REPLAY_COURSE;
            case "live" -> type = LIVE_COURSE;
            case "imageText" -> type = IMAGE_TEXT;
            case "learnPath" -> type = LEARN_PATH;
            case "book" -> type = E_BOOK;
            case "vip" -> type = VIP;
        }
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getGoodsId, goodsId)
                .eq(Order::getGoodsType, type)
                .eq(Order::getUserId, userId)
                .eq(Order::getIsSeckill, isSeckill);
        // 是开始秒杀场景时，传秒杀的时间段
        if (ObjectUtils.isNotNull(startAt) && ObjectUtils.isNotNull(endAt)) {
            wrapper.between(Order::getCreatedTime, startAt, endAt);
        }
        wrapper.last(" limit 1");
        Order one = getOne(wrapper);
        if (ObjectUtils.isNull(one)) {
            return null;
        }
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

    @Override
    public List<OrderVO> queryPayOrderList(Long userId) {
        List<Order> list = list(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_PAID)
                .eq(Order::getPayStatus, PayStatusEnum.HAVE_PAID)
                .eq(Order::getUserId, userId));
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            return list.stream().map(item -> {
                OrderVO vo = new OrderVO();
                BeanUtils.copyProperties(item, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public OrderEndFullVO getEndOrderList(Integer pageNum, Integer pageSize, String sort, String order,
                                          String orderId, String goodsName, Integer isRefund, Integer status,
                                          List<String> createdTime, String payment) {
        OrderEndFullVO vo = new OrderEndFullVO();
        // 订单不同状态数量
        vo.setCountMap(getCountMap());
        // 存放用户信息
        Map<Long, UserSimpleVO> users = getUsers();
        vo.setUsers(users);
        // 查询数量
        long count = count();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        // 订单号
        if (StringUtils.isNotBlank(orderId)) {
            wrapper.eq(Order::getOrderNumber, orderId);
        }
        // 商品名
        if (StringUtils.isNotBlank(goodsName)) {
            wrapper.like(Order::getGoodsName, goodsName);
        }
        // 是否退款 通过订单状态判断
        if (ObjectUtils.isNotNull(isRefund) && Objects.equals(isRefund, 1)) {
            wrapper.eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_REFUNDED);
        }
        // 订单状态
        if (ObjectUtils.isNotNull(status) && ObjectUtils.isNotNull(OrderStatusEnum.getByCode(status))) {
            wrapper.eq(Order::getOrderStatus, OrderStatusEnum.getByCode(status));
        }
        if (ObjectUtils.isNotNull(createdTime) && createdTime.size() > 1 && StringUtils.isNotBlank(createdTime.get(0))
                && StringUtils.isNotBlank(createdTime.get(1))) {
            // 时间格式化
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(createdTime.get(0), formatter);
            LocalDateTime end = LocalDateTime.parse(createdTime.get(1), formatter);
            // 查询时间范围
            wrapper.between(Order::getCreatedTime, start, end);
        }
        // 订单支付类型
        if (StringUtils.isNotBlank(payment) && ObjectUtils.isNotNull(PayTypeEnum.valueOf(payment))) {
            wrapper.eq(Order::getPayType, PayTypeEnum.valueOf(payment));
        }
        // 排序 通过orderId倒序
        wrapper.orderByDesc(Order::getOrderId);
        // 分页查询
        Page<Order> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setUsers(new HashMap<>(0));
            return vo;
        }
        List<OrderFullVO> list = page.getRecords().stream().map(item -> {
            OrderFullVO vo1 = new OrderFullVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setIsRefund(item.getOrderStatus().equals(OrderStatusEnum.ORDERED_AND_REFUNDED));
            vo1.setPayTypeText(item.getPayType().getValue());
            vo1.setPayment(item.getPayType().getValue());
            vo1.setOrderStatusText(item.getOrderStatus().getValue());
            // 判断商品类型，查找对应的商品信息
            if (VIP.equals(item.getGoodsType())) {
                VipVO data = userFeignClient.getVipById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            } else if (REPLAY_COURSE.equals(item.getGoodsType())) {
                CourseVO data = courseFeignClient.getCourseById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                goods.setGoodsCover(data.getCoverLink());
                vo1.setGoods(goods);
            } else if (LIVE_COURSE.equals(item.getGoodsType())) {
                LiveCourseVO data = courseFeignClient.getLiveCourseDetail(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                vo1.setGoods(goods);
            } else if (IMAGE_TEXT.equals(item.getGoodsType())) {
                ImageTextVO data = bookFeignClient.getImageTextById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getTitle());
                vo1.setGoods(goods);
            } else if (LEARN_PATH.equals(item.getGoodsType())) {
                LearnPathVO data = bookFeignClient.getPathDetail(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            } else if (E_BOOK.equals(item.getGoodsType())) {
                EBookVO data = bookFeignClient.getEBookById(item.getGoodsId()).getData();
                GoodsVO goods = new GoodsVO();
                goods.setOrderNumber(item.getOrderNumber());
                goods.setGoodsType(VIP.getValue());
                goods.setGoodsPrice(data.getPrice());
                goods.setGoodsId(data.getId());
                goods.setGoodsName(data.getName());
                vo1.setGoods(goods);
            }
            return vo1;
        }).collect(Collectors.toList());
        vo.getData().setData(list);
        vo.getData().setTotal(count);
        return vo;
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        Order order = getById(orderId);
        if (ObjectUtils.isNull(order)) {
            return new OrderDetailVO();
        }
        OrderDetailVO vo = new OrderDetailVO();
        OrderFullVO vo1 = new OrderFullVO();
        vo1.setOrderId(orderId);
        vo1.setUserId(order.getUserId());
        vo1.setAmount(order.getAmount());
        vo1.setOrderNumber(order.getOrderNumber());
        vo1.setPayment(order.getPayType().getValue());
        vo1.setPayTypeText(order.getPayType().getValue());
        vo1.setOrderStatusText(order.getOrderStatus().getValue());
        vo1.setCreatedTime(order.getCreatedTime());
        vo1.setIsRefund(order.getOrderStatus().equals(OrderStatusEnum.ORDERED_AND_REFUNDED));
        // 判断商品类型，查找对应的商品信息
        if (VIP.equals(order.getGoodsType())) {
            VipVO data = userFeignClient.getVipById(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getName());
            vo1.setGoods(goods);
        } else if (REPLAY_COURSE.equals(order.getGoodsType())) {
            CourseVO data = courseFeignClient.getCourseById(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getTitle());
            goods.setGoodsCover(data.getCoverLink());
            vo1.setGoods(goods);
        } else if (LIVE_COURSE.equals(order.getGoodsType())) {
            LiveCourseVO data = courseFeignClient.getLiveCourseDetail(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getTitle());
            vo1.setGoods(goods);
        } else if (IMAGE_TEXT.equals(order.getGoodsType())) {
            ImageTextVO data = bookFeignClient.getImageTextById(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getTitle());
            vo1.setGoods(goods);
        } else if (LEARN_PATH.equals(order.getGoodsType())) {
            LearnPathVO data = bookFeignClient.getPathDetail(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getName());
            vo1.setGoods(goods);
        } else if (E_BOOK.equals(order.getGoodsType())) {
            EBookVO data = bookFeignClient.getEBookById(order.getGoodsId()).getData();
            GoodsVO goods = new GoodsVO();
            goods.setOrderNumber(order.getOrderNumber());
            goods.setGoodsType(VIP.getValue());
            goods.setGoodsPrice(data.getPrice());
            goods.setGoodsId(data.getId());
            goods.setGoodsName(data.getName());
            vo1.setGoods(goods);
        }
        vo.setOrder(vo1);
        vo.setUser(userFeignClient.getUserById(order.getUserId() + "").getData());
        return vo;
    }

    @Override
    public BigDecimal orderStatCount(Integer type) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_PAID)
                .eq(Order::getPayStatus, PayStatusEnum.HAVE_PAID);
        // 获取上个月的第一天
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        // 获取上个月的最后一天
        LocalDate endOfLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        // 如果需要 LocalDateTime
        LocalDateTime startOfLastMonthAtStartOfDay = startOfLastMonth.atStartOfDay();
        LocalDateTime endOfLastMonthAtEndOfDay = endOfLastMonth.atTime(23, 59, 59);

        // 获取本月的第一天
        LocalDate startOfThisMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        // 获取本月的最后一天
        LocalDate endOfThisMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        // 如果需要 LocalDateTime
        LocalDateTime startOfThisMonthAtStartOfDay = startOfThisMonth.atStartOfDay();
        LocalDateTime endOfThisMonthAtEndOfDay = endOfThisMonth.atTime(23, 59, 59);
        LocalDate today = LocalDate.now();

        // 今天开始的时间
        LocalDateTime startOfToday = today.atStartOfDay();
        // 今天结束的时间
        LocalDateTime endOfToday = today.atTime(23, 59, 59);
        BigDecimal moneyCount = new BigDecimal(0);
        List<Order> list = new ArrayList<>();
        switch (type) {
            case 1 ->
                    list = list(wrapper.between(Order::getCreatedTime, startOfLastMonthAtStartOfDay, endOfLastMonthAtEndOfDay));
            case 2 ->
                    list = list((wrapper.between(Order::getCreatedTime, startOfThisMonthAtStartOfDay, endOfThisMonthAtEndOfDay)));
            case 3 -> list = list((wrapper.between(Order::getCreatedTime, startOfToday, endOfToday)));
        }
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            for (Order item : list) {
                moneyCount = moneyCount.add(item.getAmount());
            }
        }
        return moneyCount;
    }

    @Override
    public Long userStatCount(Integer type) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_PAID)
                .eq(Order::getPayStatus, PayStatusEnum.HAVE_PAID);
        LocalDate today = LocalDate.now();
        // 今天开始的时间
        LocalDateTime startOfToday = today.atStartOfDay();
        // 今天结束的时间
        LocalDateTime endOfToday = today.atTime(23, 59, 59);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        // 昨天开始的时间
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        // 昨天结束的时间
        LocalDateTime endOfYesterday = yesterday.atTime(23, 59, 59);
        Collection<Order> list = new ArrayList<>(0);
        switch (type) {
            case 1 -> list = list(wrapper.between(Order::getCreatedTime, startOfToday, endOfToday));
            case 2, 3 -> list = list((wrapper.between(Order::getCreatedTime, startOfYesterday, endOfYesterday)));
        }
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            if (type == 1) {
                // 今日支付用户数
                list = list.stream().collect(Collectors.toMap(Order::getUserId, Function.identity(), (i, e) -> i, LinkedHashMap::new)).values();
            } else if (type == 2) {
                // 昨日支付用户数
                list = list.stream().collect(Collectors.toMap(Order::getUserId, Function.identity(), (i, e) -> i, LinkedHashMap::new)).values();
            }
        }
        return (long) list.size();
    }

    @Override
    public Map<LocalDate, Long> everyDayOrderCount() {
        // 创建一个Map来存储过去七天的日期和订单数量
        Map<LocalDate, Long> result = new LinkedHashMap<>();
        List<OrderCountDTO> list = orderMapper.queryOrderCount();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            result.put(date, 0L); // 使用0初始化订单数量
        }
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            // 使用数据库的结果更新Map
            for (OrderCountDTO item : list) {
                result.put(item.getOrderDate(), item.getCount());
            }
        }
        return result;
    }

    @Override
    public Map<LocalDate, Long> everyDayOrderPay() {
        // 创建一个Map来存储过去七天的日期和订单数量
        Map<LocalDate, Long> result = new LinkedHashMap<>();
        List<OrderCountDTO> list = orderMapper.queryPayCount();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            result.put(date, 0L); // 使用0初始化订单数量
        }
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            // 使用数据库的结果更新Map
            for (OrderCountDTO item : list) {
                result.put(item.getOrderDate(), item.getCount());
            }
        }
        return result;
    }

    @Override
    public Map<LocalDate, BigDecimal> everyDayOrderMoney() {
        // 创建一个Map来存储过去七天的日期和订单数量
        Map<LocalDate, BigDecimal> result = new LinkedHashMap<>();
        List<PayCountDTO> list = orderMapper.queryPayMoney();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            result.put(date, new BigDecimal(0)); // 使用0初始化订单数量
        }
        if (ObjectUtils.isNotNull(list) && CollUtils.isNotEmpty(list)) {
            // 使用数据库的结果更新Map
            for (PayCountDTO item : list) {
                result.put(item.getOrderDate(), item.getAmount());
            }
        }
        return result;
    }

    @Override
    public Page<SellCountTopVO> querySellCountVO(Integer pageNum, Integer pageSize, String startAt, String endAt, String goodsType) {
        Integer type = null;
        if (StringUtils.isNotBlank(goodsType)) {
            type = ProductTypeEnum.valueOf(goodsType).getCode();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串解析为 LocalDateTime 对象
        LocalDateTime dateTime = LocalDateTime.parse(startAt, formatter);
        LocalDateTime dateTime1 = LocalDateTime.parse(endAt, formatter);
        // 查询
        Page<SellCountTopVO> vo = new Page<>();
        long count = orderMapper.sellTopCount(dateTime, dateTime1, type);
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        List<SellCountTopVO> list = orderMapper.getSellTopPage(0, pageSize, dateTime, dateTime1, type);
        vo.setRecords(list);
        vo.setTotal(count);
        return vo;
    }

    @Override
    public OrderGraphVO getOrderGraph(String startAt, String endAt) {
        OrderGraphVO vo = new OrderGraphVO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串解析为 LocalDateTime 对象
        LocalDateTime start = LocalDateTime.parse(startAt, formatter);
        LocalDateTime end = LocalDateTime.parse(endAt, formatter);
        // 支付金额数
        Map<LocalDate, BigDecimal> orderPayAmount = new HashMap<>(0);
        // 支付订单数
        Map<LocalDate, Long> orderPayCount = new HashMap<>(0);
        // 支付人数
        Map<LocalDate, Long> orderPayNum = new HashMap<>(0);
        // 支付均价
        Map<LocalDate, BigDecimal> orderPayAvg = new HashMap<>(0);
        // 初始化map数据
        // 遍历时间段内的每一天
        LocalDateTime current = start;
        while (!current.isAfter(end)) {
            LocalDate currentDate = current.toLocalDate();
            // 假设初始数据为0，根据实际需要调整
            orderPayCount.putIfAbsent(currentDate, 0L);
            orderPayNum.putIfAbsent(currentDate, 0L);
            orderPayAmount.putIfAbsent(currentDate, new BigDecimal(0));
            orderPayAvg.putIfAbsent(currentDate, new BigDecimal(0));
            // 增加一天
            current = current.plusDays(1);
        }
        // 查询获得数据
        List<OrderCountDTO> list1 = orderMapper.queryFixedPayCount(start, end); // 支付订单数
        List<PayCountDTO> list2 = orderMapper.queryFixedPayAvg(start, end); // 支付均价
        List<PayCountDTO> list3 = orderMapper.queryFixedPayAmount(start, end); // 支付金额
        List<OrderCountDTO> list4 = orderMapper.queryFixedPayNum(start, end); // 支付人数
        if (ObjectUtils.isNotNull(list1) && CollUtils.isNotEmpty(list1)) {
            // 使用数据库的结果更新Map
            for (OrderCountDTO item : list1) {
                orderPayCount.put(item.getOrderDate(), item.getCount());
            }
        }
        if (ObjectUtils.isNotNull(list2) && CollUtils.isNotEmpty(list2)) {
            // 使用数据库的结果更新Map
            for (PayCountDTO item : list2) {
                orderPayAvg.put(item.getOrderDate(), item.getAmount());
            }
        }
        if (ObjectUtils.isNotNull(list3) && CollUtils.isNotEmpty(list3)) {
            // 使用数据库的结果更新Map
            for (PayCountDTO item : list3) {
                orderPayAmount.put(item.getOrderDate(), item.getAmount());
            }
        }
        if (ObjectUtils.isNotNull(list4) && CollUtils.isNotEmpty(list4)) {
            // 使用数据库的结果更新Map
            for (OrderCountDTO item : list4) {
                orderPayNum.put(item.getOrderDate(), item.getCount());
            }
        }
        vo.setOrderPayAmount(orderPayAmount);
        vo.setOrderPayCount(orderPayCount);
        vo.setOrderPayAvg(orderPayAvg);
        vo.setOrderPayNum(orderPayNum);
        return vo;
    }

    /**
     * 生成订单id，订单id使用下面的规则生成
     * - 订单id一共设置成 19 位
     * - 前14位是当前时间 yyyyMMddHHmmss
     * - 后5位基于当前时间戳 使用IDWorker工具类生成
     *
     * @param now
     * @return
     */
    private String generateOrderId(LocalDateTime now) {
        // 格式化当前时间为 yyyyMMddHHmmss
        String nowTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        IDWorker snowFlake = new IDWorker(2, 3);
        long id = snowFlake.nextId();
        String str = String.valueOf(id);
        int length = str.length();
        return nowTime + str.substring(length - 5);
    }

    /**
     * 将订单存入数据库
     *
     * @param form
     * @param vo
     */
    private void handle2MySQL(CreateOrderForm form, OrderVO vo) {
        Order order = new Order();
        order.setUserId(vo.getUserId());
        order.setOrderNumber(vo.getOrderId());
        order.setGoodsPrice(vo.getGoodsPrice());
        order.setGoodsName(form.getGoodsName());
        order.setAmount(vo.getAmount());
        order.setPayType(PayTypeEnum.valueOf(form.getPayment()));
        order.setOrderNotes(vo.getOrderNotes());
        order.setGoodsId(form.getGoodsId());
        order.setOrderStatus(OrderStatusEnum.ORDERED_NO_PAY);
        order.setGoodsType(ProductTypeEnum.valueOf(form.getGoodsType()));
        order.setIsUseCoupon(!vo.getGoodsDiscount().equals(BigDecimal.ZERO));
        order.setCouponId(StringUtils.isBlank(form.getPromoCode()) ? null : Long.valueOf(form.getPromoCode()));
        order.setPayStatus(PayStatusEnum.NON_PAYMENT);
        order.setCancelTime(vo.getCancelTime());
        save(order);
    }

    /**
     * 返回所有状态的订单数量
     *
     * @return
     */
    private Map<Integer, Long> getCountMap() {
        Map<Integer, Long> res = new HashMap<>();
        // 查找所有状态的订单数量
        long count1 = count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_NO_PAY));
        long count2 = count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_CANCELLED));
        long count3 = count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_PAID));
        long count4 = count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatusEnum.ORDERED_AND_REFUNDED));
        res.put(1, count1);
        res.put(2, count2);
        res.put(3, count3);
        res.put(4, count4);
        return res;
    }

    /**
     * 获取所有订单的用户信息
     *
     * @return
     */
    private Map<Long, UserSimpleVO> getUsers() {
        Map<Long, UserSimpleVO> users = new TreeMap<>();
        List<Long> ids = list(new LambdaQueryWrapper<Order>()
                .select(Order::getUserId))
                .stream().map(Order::getUserId)
                .distinct()
                .collect(Collectors.toList());
        ids.stream().forEach(item -> {
            users.put(item, userFeignClient.getUserById(item + "").getData());
        });
        return users;
    }
}
