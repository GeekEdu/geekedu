package com.zch.trade.controller;

import com.zch.api.dto.trade.coupon.CouponForm;
import com.zch.api.vo.trade.coupon.CouponVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final ICouponService couponService;

    /**
     * 获取优惠券列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    @GetMapping("/list")
    public PageResult<CouponVO> getCouponList(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(value = "keywords", required = false) String keywords) {
        return PageResult.success(couponService.getCouponList(pageNum, pageSize, keywords));
    }

    /**
     * 添加优惠券
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addCoupon(@RequestBody CouponForm form) {
        return Response.judge(couponService.addCoupon(form));
    }

}
