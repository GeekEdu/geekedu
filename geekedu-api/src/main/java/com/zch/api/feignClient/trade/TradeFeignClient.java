package com.zch.api.feignClient.trade;

import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.trade.pay.PayInfoVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

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

    /**
     * 订单是否已支付
     * @param userId
     * @param goodsId
     * @param goodsType
     * @return
     */
    @GetMapping("/api/order/v2/queryIsPay")
    Response<Boolean> queryOrderIsPay(@RequestParam("userId") Long userId,
                                             @RequestParam("goodsId") Integer goodsId,
                                             @RequestParam("goodsType") String goodsType);

    /**
     * 查询支付信息
     * @param orderId
     * @return
     */
    @GetMapping("/api/pay/info")
    Response<PayInfoVO> queryPayInfo(@RequestParam("orderId") String orderId);

    /**
     * 根据商品信息查询订单
     * @param goodsId
     * @param goodsType
     * @param userId
     * @param isSeckill
     * @return
     */
    @GetMapping("/api/order/v2/queryOrderByGoods")
    Response<OrderVO> queryOrderInfoByGoods(@RequestParam("goodsId") Integer goodsId,
                                                   @RequestParam("goodsType") String goodsType,
                                                   @RequestParam("userId") Long userId,
                                                   @RequestParam("isSeckill") Boolean isSeckill,
                                            @RequestParam(value = "startAt", required = false) LocalDateTime startAt,
                                            @RequestParam(value = "endAt", required = false) LocalDateTime endAt);

}
