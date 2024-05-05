package com.zch.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.coupon.CouponForm;
import com.zch.api.vo.trade.coupon.CouponVO;
import com.zch.api.vo.trade.coupon.UserCouponVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.trade.domain.po.Coupon;
import com.zch.trade.domain.po.CouponCode;
import com.zch.trade.domain.po.UserCoupon;
import com.zch.trade.mapper.CouponMapper;
import com.zch.trade.service.ICouponCodeService;
import com.zch.trade.service.ICouponService;
import com.zch.trade.service.IUserCouponService;
import com.zch.trade.utils.CodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zch.common.redis.constants.RedisConstants.COUPON_CODE_SERIAL;
import static com.zch.common.redis.constants.RedisConstants.COUPON_RANGE_KEY;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    private final IUserCouponService userCouponService;

    private final ICouponCodeService couponCodeService;

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
        // 生成优惠券时 会生成优惠码
        // TODO
        BeanUtils.copyProperties(form, coupon);
        coupon.setCreatedTime(LocalDateTime.now());
        return save(coupon);
    }

    @Override
    public List<UserCouponVO> getUserCounponList() {
        List<UserCouponVO> vo = new ArrayList<>();
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        List<UserCoupon> list = userCouponService.list(new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getUserId, userId));
        if (ObjectUtils.isNull(list) && CollUtils.isEmpty(list)) {
            return vo;
        }
        vo = list.stream().map(item -> {
            UserCouponVO vo1 = new UserCouponVO();
            BeanUtils.copyProperties(item, vo1);
            // 查找优惠券的详细信息
            Coupon coupon = getById(item.getCouponId());
            if (ObjectUtils.isNotNull(coupon)) {
                vo1.setCouponCode(coupon.getCouponCode());
                vo1.setCouponDesc(coupon.getCouponDesc());
                vo1.setCouponLimit(coupon.getCouponLimit());
                vo1.setCouponPrice(coupon.getCouponPrice());
                vo1.setCreatedTime(coupon.getCreatedTime());
                vo1.setExpiredTime(coupon.getExpiredTime());
                vo1.setUseLimit(coupon.getUseLimit());
                // 查看优惠券是否过期
                if (LocalDateTime.now().isAfter(coupon.getExpiredTime())) {
                    vo1.setIsExpired(true);
                } else {
                    vo1.setIsExpired(false);
                }
            }
            return vo1;
        }).collect(Collectors.toList());
        return vo;
    }

    /**
     * 异步生成优惠码
     * @param coupon
     */
    @Async("generateCouponCodeExecutor")
    public void asyncGenerateCode(Coupon coupon) {
        // 发放数量
        Integer totalNum = coupon.getCouponTotal();
        // 获取Redis自增序列号
        Long result = RedisUtils.getAtomIncrValue(COUPON_CODE_SERIAL, totalNum);
        int maxSerialNum = result.intValue();
        List<CouponCode> list = new ArrayList<>(totalNum);
        for (int serialNum = maxSerialNum - totalNum + 1; serialNum <= maxSerialNum; serialNum++) {
            // 生成兑换码
            String code = CodeUtils.generateCode(serialNum, coupon.getCouponId());
            CouponCode c = new CouponCode();
            c.setCode(code);
            c.setId(serialNum);
            c.setCouponId(coupon.getCouponId());
            c.setCreatedTime(coupon.getCreatedTime());
            c.setExpiredTime(coupon.getExpiredTime());
            list.add(c);
        }
        // 保存数据库
        couponCodeService.saveBatch(list);
        // 写入Redis缓存
        RedisUtils.addRSetSingle(COUPON_RANGE_KEY, maxSerialNum, coupon.getCouponId());
    }
}
