package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderDetailVO;
import com.zch.api.vo.trade.order.OrderEndFullVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.trade.order.SellCountTopVO;
import com.zch.trade.domain.po.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param form
     * @return
     */
    OrderVO createOrder(CreateOrderForm form);

    /**
     * 前台获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<OrderFullVO> getOrderPage(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 前台判断订单是否存在，且购买
     * @param userId
     * @param goodsId
     * @param
     */
    Boolean queryOrderIsPay(Long userId, Integer goodsId, String goodsType);

    /**
     * 更新订单信息
     * @param orderNumber
     * @return
     */
    Boolean updateOrderStatus(String orderNumber);

    /**
     * 根据商品查询订单
     * @param goodsId
     * @param goodsType
     * @param userId
     * @param isSeckill
     * @return
     */
    OrderVO queryOrderByGoods(Integer goodsId, String goodsType, Long userId, Boolean isSeckill, LocalDateTime startAt, LocalDateTime endAt);

    //===================================================

    /**
     * 后台 获取订单列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param orderId
     * @param goodsName
     * @param isRefund
     * @param status
     * @param createdTime
     * @param payment
     * @return
     */
    OrderEndFullVO getEndOrderList(Integer pageNum, Integer pageSize, String sort, String order,
                                   String orderId, String goodsName, Integer isRefund, Integer status,
                                   List<String> createdTime, String payment);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderDetailVO getOrderDetail(Long orderId);

    /**
     * 统计订单数据
     * @param type
     * @return
     */
    BigDecimal orderStatCount(Integer type);

    /**
     * 统计用户数据
     * @param type
     * @return
     */
    Long userStatCount(Integer type);

    /**
     * 每日创建订单数
     * @return
     */
    Map<LocalDate, Long> everyDayOrderCount();

    /**
     * 每日已支付订单
     * @return
     */
    Map<LocalDate, Long> everyDayOrderPay();

    /**
     * 每日收入
     * @return
     */
    Map<LocalDate, BigDecimal> everyDayOrderMoney();

    Page<SellCountTopVO> querySellCountVO(Integer pageNum, Integer pageSize, String startAt, String endAt, String goodsType);

}
