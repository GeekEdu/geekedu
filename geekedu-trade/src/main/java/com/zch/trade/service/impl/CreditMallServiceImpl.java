package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.GoodsForm;
import com.zch.api.vo.trade.creditmall.CreditFullVO;
import com.zch.api.vo.trade.creditmall.CreditMallVO;
import com.zch.api.vo.trade.creditmall.GoodsTypeVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.trade.domain.po.CreditMall;
import com.zch.trade.enums.GoodsTypeEnums;
import com.zch.trade.mapper.CreditMallMapper;
import com.zch.trade.service.ICreditMallService;
import com.zch.trade.service.IGoodsTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CreditMallServiceImpl extends ServiceImpl<CreditMallMapper, CreditMall> implements ICreditMallService {

    private final IGoodsTypeService goodsTypeService;

    @Override
    public CreditFullVO getMallListByCondition(Integer pageNum, Integer pageSize, String keywords, String type) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        CreditFullVO vo = new CreditFullVO();
        vo.setGoodsType(getGoodsType());
        long count = count();
        if (count == 0) {
            vo.getGoods().setData(new ArrayList<>(0));
            vo.getGoods().setTotal(0);
            return vo;
        }
        LambdaQueryWrapper<CreditMall> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(CreditMall::getTitle, keywords);
        }
        if (StringUtils.isNotBlank(type)) {
            wrapper.eq(CreditMall::getTypeId, GoodsTypeEnums.valueOf(type));
        }
        Page<CreditMall> page = page(new Page<CreditMall>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getGoods().setData(new ArrayList<>(0));
            vo.getGoods().setTotal(0);
            return vo;
        }
        List<CreditMallVO> list = page.getRecords().stream().map(item -> {
            CreditMallVO vo1 = new CreditMallVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setGoodsType(item.getTypeId() == null ? "" : item.getTypeId().getValue());
            return vo1;
        }).collect(Collectors.toList());
        vo.getGoods().setData(list);
        vo.getGoods().setTotal(page.getTotal());
        return vo;
    }

    @Transactional
    @Override
    public Boolean deleteGoodById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public CreditMallVO getGoodDetailById(Integer id) {
        CreditMall one = getById(id);
        if (ObjectUtils.isNull(one)) {
            return new CreditMallVO();
        }
        CreditMallVO vo = new CreditMallVO();
        BeanUtils.copyProperties(one, vo);
        vo.setGoodsType(one.getTypeId() == null ? "" : one.getTypeId().getValue());
        return vo;
    }

    @Override
    public Boolean addGood(GoodsForm form) {
        CreditMall good = new CreditMall();
        good.setTitle(form.getTitle());
        good.setCover(form.getCover());
        good.setPrice(form.getPrice());
        good.setStockCount(form.getStockCount());
        good.setIntro(form.getIntro());
        good.setIsShow(form.getIsShow());
        good.setIsVirtual(form.getIsVirtual());
        good.setTypeId(form.getIsVirtual() ? GoodsTypeEnums.valueOf(form.getGoodsType()) : null);
        good.setVId(form.getIsVirtual() ? form.getVId() : null);
        return save(good);
    }

    @Override
    public Boolean updateGood(Integer id, GoodsForm form) {
        CreditMall good = getById(id);
        good.setTitle(form.getTitle());
        good.setCover(form.getCover());
        good.setPrice(form.getPrice());
        good.setStockCount(form.getStockCount());
        good.setIntro(form.getIntro());
        good.setIsShow(form.getIsShow());
        return updateById(good);
    }

    @Override
    public Page<CreditMallVO> getMallListByPage(Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        Page<CreditMallVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        Page<CreditMall> page = page(new Page<CreditMall>(pageNum, pageSize), new LambdaQueryWrapper<CreditMall>());
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            CreditMallVO vo1 = new CreditMallVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setGoodsType(item.getTypeId() == null ? "" : item.getTypeId().getValue());
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(page.getTotal());
        return vo;
    }

    /**
     * 获取商品分类列表
     * @return
     */
    private List<GoodsTypeVO> getGoodsType() {
        return goodsTypeService.getGoodsTypeList();
    }
}
