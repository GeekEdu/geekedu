package com.zch.trade.service.impl;

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
import com.zch.api.vo.trade.order.GoodsVO;
import com.zch.api.vo.trade.order.OrderDetailVO;
import com.zch.api.vo.trade.order.OrderEndFullVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.api.vo.user.VipVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        }
        // 用户id
        Long userId = UserContext.getLoginId();
        vo.setUserId(userId);
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
        // TODO 通知消息队列
        // 写入支付信息
        PayInfoForm form1 = new PayInfoForm();
        form1.setPayName(vo.getGoodsName());
        form1.setOrderId(orderId);
        form1.setPayChannel(form.getPayment());
        form1.setPayAmount(vo.getAmount());
        form1.setOrderId(vo.getOrderId());
        tradeFeignClient.createPayInfo(form1);
        // 存入数据库
        handle2MySQL(form, vo);
        return vo;
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
        if (ObjectUtils.isNotNull(createdTime)  && createdTime.size() > 1 && StringUtils.isNotBlank(createdTime.get(0))
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

    /**
     * 生成订单id，订单id使用下面的规则生成
     *   - 订单id一共设置成 19 位
     *   - 前14位是当前时间 yyyyMMddHHmmss
     *   - 后5位基于当前时间戳 使用IDWorker工具类生成
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
     * @param form
     * @param vo
     */
    private void handle2MySQL(CreateOrderForm form, OrderVO vo) {
        Order order = new Order();
        order.setOrderNumber(vo.getOrderId());
        order.setGoodsPrice(vo.getGoodsPrice());
        order.setGoodsName(vo.getGoodsName());
        order.setAmount(vo.getAmount());
        order.setPayType(PayTypeEnum.valueOf(form.getPayment()));
        order.setOrderNotes(vo.getOrderNotes());
        order.setGoodsId(form.getGoodsId());
        order.setOrderStatus(OrderStatusEnum.ORDERED_NO_PAY);
        order.setGoodsType(ProductTypeEnum.valueOf(form.getGoodsType()));
        order.setIsUseCoupon(! vo.getGoodsDiscount().equals(BigDecimal.ZERO));
        order.setCouponId(StringUtils.isBlank(form.getPromoCode()) ? null : Long.valueOf(form.getPromoCode()));
        order.setPayStatus(PayStatusEnum.NON_PAYMENT);
        order.setCancelTime(vo.getCancelTime());
        save(order);
    }

    /**
     * 返回所有状态的订单数量
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
