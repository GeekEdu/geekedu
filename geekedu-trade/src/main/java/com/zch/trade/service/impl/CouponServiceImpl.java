package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.coupon.CouponForm;
import com.zch.api.vo.trade.coupon.CouponVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.trade.domain.po.Coupon;
import com.zch.trade.mapper.CouponMapper;
import com.zch.trade.service.ICouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {
    @Override
    public Page<CouponVO> getCouponList(Integer pageNum, Integer pageSize, String keywords) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Coupon::getCouponCode, keywords);
        }
        Page<CouponVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        Page<Coupon> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        List<CouponVO> list = page.getRecords().stream().map(item -> {
            CouponVO vo1 = new CouponVO();
            BeanUtils.copyProperties(item, vo1);
            return vo1;
        }).collect(Collectors.toList());
        vo.setRecords(list);
        vo.setTotal(count);
        return vo;
    }

    @Override
    public Boolean addCoupon(CouponForm form) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(form, coupon);
        coupon.setCreatedTime(LocalDateTime.now());
        return save(coupon);
    }
}
