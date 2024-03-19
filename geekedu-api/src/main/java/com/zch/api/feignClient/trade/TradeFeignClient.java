package com.zch.api.feignClient.trade;

import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.common.mvc.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@FeignClient(contextId = "trade", name = "trade-service", configuration = FeignInterceptor.class)
public interface TradeFeignClient {

    /**
     * 写入支付信息
     * @param form
     */
    @PostMapping("/api/pay/create/payInfo")
    void createPayInfo(@RequestBody PayInfoForm form);

    /**
     * 订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/api/order/v2/list")
    PageResult<OrderFullVO> getOrderPage(@RequestParam("userId") Long userId, @RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize);

}
