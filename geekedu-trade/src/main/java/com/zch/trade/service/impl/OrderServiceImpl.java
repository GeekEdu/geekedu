package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.order.OrderVO;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
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
import java.util.Objects;

import static com.zch.trade.enums.ProductTypeEnum.VIP;

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
        vo.setOrderId(generateOrderId(now));
        // TODO 通知消息队列
        // 写入支付信息
        PayInfoForm form1 = new PayInfoForm();
        form1.setPayName(vo.getGoodsName());
        form1.setPayChannel(form.getPayment());
        form1.setPayAmount(vo.getAmount());
        form1.setOrderId(vo.getOrderId());
        tradeFeignClient.createPayInfo(form1);
        // 存入数据库
        handle2MySQL(form, vo);
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
}
