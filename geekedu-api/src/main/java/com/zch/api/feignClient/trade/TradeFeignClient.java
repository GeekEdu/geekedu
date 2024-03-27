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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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

    /**
     * 用于统计订单金额相关数据
     * @param type 1-上月收入 2-本月收入 3-今日收入
     * @return
     */
    @GetMapping("/api/order/stat/moneyCount")
    Response<BigDecimal> orderStatCount(@RequestParam("type") Integer type);

    /**
     * 用于统计订单用户总数
     * @param type 1-今日支付用户数 2-昨日支付数 3-昨日支付用户数
     * @return
     */
    @GetMapping("/api/order/stat/userCount")
    Response<Long> userStatCount(@RequestParam("type") Integer type);

    /**
     * 每日创建订单数
     * @return
     */
    @GetMapping("/api/order/date/orderCount")
    Response<Map<LocalDate, Long>> everyDayOrderCount();

    /**
     * 每日已支付订单
     * @return
     */
    @GetMapping("/api/order/date/orderPay")
    Response<Map<LocalDate, Long>> everyDayOrderPay();

    /**
     * 每日收入
     * @return
     */
    @GetMapping("/api/order/date/orderMoney")
    Response<Map<LocalDate, BigDecimal>> everyDayOrderMoney();

}
