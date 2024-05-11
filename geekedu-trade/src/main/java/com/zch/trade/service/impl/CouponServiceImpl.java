package com.zch.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.coupon.CouponForm;
import com.zch.api.vo.trade.coupon.CouponVO;
import com.zch.api.vo.trade.coupon.UserCouponVO;
import com.zch.common.core.utils.*;
import com.zch.common.mvc.exception.CommonException;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zch.common.redis.constants.RedisConstants.*;

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

//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final RedisScript<Long> RECEIVE_COUPON_SCRIPT;
//    private static final RedisScript<String> EXCHANGE_COUPON_SCRIPT;
//
//    static {
//        RECEIVE_COUPON_SCRIPT = RedisScript.of(new ClassPathResource("lua/receive_coupon.lua"), Long.class);
//        EXCHANGE_COUPON_SCRIPT = RedisScript.of(new ClassPathResource("lua/exchange_coupon.lua"), String.class);
//    }

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

    @Override
    public void receiveCoupon(Long couponId) {
//        // 1.执行LUA脚本，判断结果
//        // 1.1.准备参数
//        String key1 = COUPON_CACHE_KEY_PREFIX + couponId;
//        String key2 = USER_COUPON_CACHE_KEY_PREFIX + couponId;
//        long userId = Long.parseLong((String) StpUtil.getLoginId());
//        // 1.2.执行脚本
//        Long r = redisTemplate.execute(RECEIVE_COUPON_SCRIPT, List.of(key1, key2), Long.toString(userId));
//        int result = NumberUtils.null2Zero(r).intValue();
//        if (result != 0) {
//            // 结果大于0，说明出现异常
//            throw new CommonException("发放优惠码失败");
//        }
    }

    @Override
    public void exchangeCoupon(String code) {
//        // 1.校验并解析兑换码
//        long serialNum = CodeUtils.parseCode(code);
//        // 2.执行LUA脚本
//        long userId = Long.parseLong((String) StpUtil.getLoginId());
//        String result = redisTemplate.execute(
//                EXCHANGE_COUPON_SCRIPT,
//                List.of(COUPON_CODE_MAP_KEY, COUPON_RANGE_KEY),
//                String.valueOf(serialNum), String.valueOf(serialNum + 5000), Long.toString(userId));
//        long r = NumberUtils.parseLong(result);
//        if (r < 10) {
//            // 异常结果应该是在1~5之间
//            throw new CommonException("兑换优惠码失败");
//        }
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
