package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.trade.seckill.SecondKillVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.redis.utils.StringUtils;
import com.zch.trade.domain.po.SecondKill;
import com.zch.trade.mapper.SecondKillMapper;
import com.zch.trade.service.ISecondKillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SecondKillServiceImpl extends ServiceImpl<SecondKillMapper, SecondKill> implements ISecondKillService {
    @Override
    public Page<SecondKillVO> querySecKillList(Integer pageNum, Integer pageSize, String sort, String order, String keywords) {
        Page<SecondKillVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<SecondKill> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(SecondKill::getGoodsTitle, keywords);
        }
        if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
            wrapper.orderBy(true, "asc".equals(order), SecondKill::getCreatedTime);
        }
        Page<SecondKill> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            SecondKillVO vo1 = new SecondKillVO();
            BeanUtils.copyProperties(item, vo1);
            // 看当前秒杀是否开始
            if (LocalDateTime.now().isBefore(item.getStartAt())) {
                vo1.setIsStart(false);
            } else {
                vo1.setIsStart(true);
            }
            // 看当前秒杀是否结束
            if (LocalDateTime.now().isAfter(item.getEndAt())) {
                vo1.setIsOver(true);
            } else {
                vo1.setIsOver(false);
            }
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(count);
        return vo;
    }
}
