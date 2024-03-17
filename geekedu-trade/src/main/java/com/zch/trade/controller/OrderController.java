package com.zch.trade.controller;

import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
