package com.zch.trade.controller;

import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.common.mvc.result.Response;
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

    @PostMapping("/create")
    public Response<OrderVO> createOrder(@RequestBody CreateOrderForm form) {
        long now = System.currentTimeMillis() / 1000;
        OrderVO vo = new OrderVO();
        vo.setOrderId(String.valueOf(now));
        return Response.success(vo);
    }

}
