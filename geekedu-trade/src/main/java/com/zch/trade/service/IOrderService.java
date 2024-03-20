package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderDetailVO;
import com.zch.api.vo.trade.order.OrderEndFullVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.trade.domain.po.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

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

}
