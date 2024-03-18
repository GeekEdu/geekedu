package com.zch.api.feignClient.trade;

import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public void createPayInfo(@RequestBody PayInfoForm form);

}
