package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.trade.domain.po.CouponCode;
import com.zch.trade.mapper.CouponCodeMapper;
import com.zch.trade.service.ICouponCodeService;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/4/17
 */
@Service
public class CouponCodeServiceImpl extends ServiceImpl<CouponCodeMapper, CouponCode> implements ICouponCodeService {
}
