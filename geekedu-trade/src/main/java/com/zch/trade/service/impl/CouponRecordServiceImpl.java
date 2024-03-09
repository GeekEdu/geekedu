package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.trade.domain.po.CouponRecord;
import com.zch.trade.mapper.CouponRecordMapper;
import com.zch.trade.service.ICouponRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CouponRecordServiceImpl extends ServiceImpl<CouponRecordMapper, CouponRecord> implements ICouponRecordService {
}
