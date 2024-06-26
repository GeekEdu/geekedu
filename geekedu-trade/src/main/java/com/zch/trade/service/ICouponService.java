package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.coupon.CouponForm;
import com.zch.api.vo.trade.coupon.CouponVO;
import com.zch.api.vo.trade.coupon.UserCouponVO;
import com.zch.trade.domain.po.Coupon;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface ICouponService extends IService<Coupon> {

    /**
     * 获取优惠券列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @return
     */
    Page<CouponVO> getCouponList(Integer pageNum, Integer pageSize, String keywords);

    /**
     * 添加优惠券
     * @param form
     * @return
     */
    Boolean addCoupon(CouponForm form);

    /**
     * 我的优惠券列表
     * @return
     */
    List<UserCouponVO> getUserCounponList();

    /**
     * 领取优惠券
     * @param couponId
     */
    void receiveCoupon(Long couponId);

    /**
     * 兑换优惠券
     * @param code
     */
    void exchangeCoupon(String code);

}
