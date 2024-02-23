package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.book.EBookAndCategoryVO;
import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.book.domain.po.EBook;
import com.zch.book.mapper.EBookMapper;
import com.zch.book.service.IEBookService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EBookServiceImpl extends ServiceImpl<EBookMapper, EBook> implements IEBookService {

    private final EBookMapper bookMapper;

    private final LabelFeignClient labelFeignClient;

    @Override
    public EBookAndCategoryVO getEBookPageByCondition(Integer pageNum, Integer pageSize, String sort, String order,
                                                      String keywords,
                                                      Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(keywords) || ObjectUtils.isNull(categoryId)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        EBookAndCategoryVO vo = new EBookAndCategoryVO();
        long count = count();
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        LambdaQueryWrapper<EBook> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(EBook::getName, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(EBook::getCategoryId, categoryId);
        }
        // 增加排序
        wrapper.orderBy(true, "asc".equals(order), EBook::getId);
        Page<EBook> page = page(new Page<EBook>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        List<EBook> records = page.getRecords();
        if (ObjectUtils.isNull(records) || CollUtils.isEmpty(records)) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
        }
        List<EBookVO> list = new ArrayList<>(records.size());
        for (EBook book : records) {
            EBookVO vo1 = new EBookVO();
            BeanUtils.copyProperties(book, vo1);
            Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(book.getCategoryId(), "E_BOOK");
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res.getData());
            list.add(vo1);
        }
        vo.getData().setTotal(count);
        vo.getData().setData(list);
        Response<List<CategorySimpleVO>> response = labelFeignClient.getCategoryList("E_BOOK");
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData())) {
            vo.setCategories(null);
        }
        vo.setCategories(response.getData());
        return vo;
    }
}
