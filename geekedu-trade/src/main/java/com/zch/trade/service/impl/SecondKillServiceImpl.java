package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.seckill.SecKillForm;
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

    @Override
    public SecondKillVO querySecKillDetail(Integer id) {
        SecondKill kill = getById(id);
        if (ObjectUtils.isNull(kill)) {
            return null;
        }
        SecondKillVO vo = new SecondKillVO();
        BeanUtils.copyProperties(kill, vo);
        // 看当前秒杀是否开始
        if (LocalDateTime.now().isBefore(kill.getStartAt())) {
            vo.setIsStart(false);
        } else {
            vo.setIsStart(true);
        }
        // 看当前秒杀是否结束
        if (LocalDateTime.now().isAfter(kill.getEndAt())) {
            vo.setIsOver(true);
        } else {
            vo.setIsOver(false);
        }
        return vo;
    }

    @Override
    public Boolean addSecKill(SecKillForm form) {
        SecondKill kill = new SecondKill();
        kill.setTotal(form.getTotal());
        kill.setGoodsCover(form.getGoodsCover());
        kill.setGoodsId(form.getGoodsId());
        kill.setGoodsTitle(form.getGoodsTitle());
        kill.setGoodsType(form.getGoodsType());
        kill.setOriginPrice(form.getOriginPrice());
        kill.setPrice(form.getPrice());
        kill.setStock(form.getStock());
        kill.setEndAt(form.getEndAt());
        kill.setStartAt(form.getStartAt());
        switch (kill.getGoodsType()) {
            case "course" -> kill.setGoodsTypeText("录播课程");
            case "live" -> kill.setGoodsTypeText("直播课程");
            case "book" -> kill.setGoodsTypeText("电子书");
        }
        return save(kill);
    }

    @Override
    public Boolean updateSecKill(Integer id, SecKillForm form) {
        SecondKill kill = getById(id);
        kill.setTotal(form.getTotal());
        kill.setGoodsCover(form.getGoodsCover());
        kill.setGoodsId(form.getGoodsId());
        kill.setGoodsTitle(form.getGoodsTitle());
        kill.setGoodsType(form.getGoodsType());
        kill.setOriginPrice(form.getOriginPrice());
        kill.setPrice(form.getPrice());
        kill.setStock(form.getStock());
        kill.setEndAt(form.getEndAt());
        kill.setStartAt(form.getStartAt());
        switch (kill.getGoodsType()) {
            case "course" -> kill.setGoodsTypeText("录播课程");
            case "live" -> kill.setGoodsTypeText("直播课程");
            case "book" -> kill.setGoodsTypeText("电子书");
        }
        return updateById(kill);
    }

    @Override
    public Boolean deleteSecKill(Integer id) {
        return removeById(id);
    }

    @Override
    public SecondKillVO getV2Detail(Integer goodsId, String goodsType) {
        SecondKill secondKill = getOne(new LambdaQueryWrapper<SecondKill>()
                .eq(SecondKill::getGoodsId, goodsId)
                .eq(SecondKill::getGoodsType, goodsType)
                // 找出未过期的秒杀
                .gt(SecondKill::getEndAt, LocalDateTime.now())
                .last("limit 1"));
        if (ObjectUtils.isNull(secondKill)) {
            return new SecondKillVO();
        }
        SecondKillVO vo = new SecondKillVO();
        BeanUtils.copyProperties(secondKill, vo);
        // 看当前秒杀是否开始
        if (LocalDateTime.now().isBefore(secondKill.getStartAt())) {
            vo.setIsStart(false);
        } else {
            vo.setIsStart(true);
        }
        // 看当前秒杀是否结束
        if (LocalDateTime.now().isAfter(secondKill.getEndAt())) {
            vo.setIsOver(true);
        } else {
            vo.setIsOver(false);
        }
        return vo;
    }
}
