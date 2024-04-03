package com.zch.trade.controller;

import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.*;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    /**
     * 创建订单
     * @param form
     * @return
     */
    @PostMapping("/create")
    public Response<OrderVO> createOrder(@RequestBody CreateOrderForm form) {
        return Response.success(orderService.createOrder(form));
    }

    /**
     * 订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/list")
    public PageResult<OrderFullVO> getOrderPage(@RequestParam("userId") Long userId,
                                                @RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(orderService.getOrderPage(userId, pageNum, pageSize));
    }

    /**
     * 订单是否已支付
     * @param userId
     * @param goodsId
     * @param goodsType
     * @return
     */
    @GetMapping("/v2/queryIsPay")
    public Response<Boolean> queryOrderIsPay(@RequestParam("userId") Long userId,
                                              @RequestParam("goodsId") Integer goodsId,
                                              @RequestParam("goodsType") String goodsType) {
        return Response.success(orderService.queryOrderIsPay(userId, goodsId, goodsType));
    }

    /**
     * 根据商品信息查询订单
     * @param goodsId
     * @param goodsType
     * @param userId
     * @param isSeckill
     * @return
     */
    @GetMapping("/v2/queryOrderByGoods")
    public Response<OrderVO> queryOrderInfoByGoods(@RequestParam("goodsId") Integer goodsId,
                                                   @RequestParam("goodsType") String goodsType,
                                                   @RequestParam("userId") Long userId,
                                                   @RequestParam("isSeckill") Boolean isSeckill,
                                                   @RequestParam(value = "startAt", required = false) LocalDateTime startAt,
                                                   @RequestParam(value = "endAt", required = false) LocalDateTime endAt) {
        return Response.success(orderService.queryOrderByGoods(goodsId, goodsType, userId, isSeckill, startAt, endAt));
    }

    /**
     * 查询用户已支付订单信息
     * @param userId
     * @return
     */
    @GetMapping("/pay/list")
    public Response<List<OrderVO>> queryPayOrderList(@RequestParam("userId") Long userId) {
        return Response.success(orderService.queryPayOrderList(userId));
    }

    // ================================================================================
    // 后台

    /**
     * 订单列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param orderId 订单号
     * @param goodsName 商品名
     * @param isRefund 是否退款 -1 全查 0-无退款 1-已退款
     * @param status 订单状态 1-未支付 2-已取消 3-已支付 4-已退款
     * @param createdTime 创单时间 数组
     * @param payment 支付方式 ALIPAY WX_PAY OTHER
     * @return
     */
    @GetMapping("/list")
    public Response<OrderEndFullVO> getEndOrderList(@RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("sort") String sort,
                                                    @RequestParam("order") String order,
                                                    @RequestParam(value = "orderId", required = false) String orderId,
                                                    @RequestParam(value = "goodsName", required = false) String goodsName,
                                                    @RequestParam(value = "isRefund", required = false) Integer isRefund,
                                                    @RequestParam(value = "status", required = false) Integer status,
                                                    @RequestParam(value = "createdTime", required = false) List<String> createdTime,
                                                    @RequestParam(value = "payment", required = false) String payment) {
        return Response.success(orderService.getEndOrderList(pageNum, pageSize, sort, order, orderId, goodsName, isRefund, status, createdTime, payment));
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/{id}/detail")
    public Response<OrderDetailVO> getOrderDetail(@PathVariable("id") Long orderId) {
        return Response.success(orderService.getOrderDetail(orderId));
    }

    //================================================
    // 统计数据
    /**
     * 用于统计订单金额相关数据
     * @param type 1-上月收入 2-本月收入 3-今日收入
     * @return
     */
    @GetMapping("/stat/moneyCount")
    public Response<BigDecimal> orderStatCount(@RequestParam("type") Integer type) {
        return Response.success(orderService.orderStatCount(type));
    }

    /**
     * 用于统计订单用户总数
     * @param type 1-今日支付用户数 2-昨日支付数 3-昨日支付用户数
     * @return
     */
    @GetMapping("/stat/userCount")
    public Response<Long> userStatCount(@RequestParam("type") Integer type) {
        return Response.success(orderService.userStatCount(type));
    }

    /**
     * 每日创建订单数
     * @return
     */
    @GetMapping("/date/orderCount")
    public Response<Map<LocalDate, Long>> everyDayOrderCount() {
        return Response.success(orderService.everyDayOrderCount());
    }

    /**
     * 每日已支付订单
     * @return
     */
    @GetMapping("/date/orderPay")
    public Response<Map<LocalDate, Long>> everyDayOrderPay() {
        return Response.success(orderService.everyDayOrderPay());
    }

    /**
     * 每日收入
     * @return
     */
    @GetMapping("/date/orderMoney")
    public Response<Map<LocalDate, BigDecimal>> everyDayOrderMoney() {
        return Response.success(orderService.everyDayOrderMoney());
    }

    /**
     * 前10销量商品
     * @param pageNum
     * @param pageSize
     * @param startAt
     * @param endAt
     * @param goodsType
     * @return
     */
    @GetMapping("/stat/sellCountTop")
    public PageResult<SellCountTopVO> querySellCountTop(@RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("startAt") String startAt,
                                                        @RequestParam("endAt") String endAt,
                                                        @RequestParam(value = "goodsType", required = false) String goodsType) {
        return PageResult.success(orderService.querySellCountVO(pageNum, pageSize, startAt, endAt, goodsType));
    }

    /**
     * 订单统计图
     * @param startAt
     * @param endAt
     * @return
     */
    @GetMapping("/stat/orderGraph")
    public Response<OrderGraphVO> getStatOrderGraph(@RequestParam("startAt") String startAt,
                                                    @RequestParam("endAt") String endAt) {
        return Response.success(orderService.getOrderGraph(startAt, endAt));
    }

}
