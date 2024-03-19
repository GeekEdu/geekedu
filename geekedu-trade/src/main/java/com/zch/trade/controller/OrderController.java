package com.zch.trade.controller;

import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderDetailVO;
import com.zch.api.vo.trade.order.OrderEndFullVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
