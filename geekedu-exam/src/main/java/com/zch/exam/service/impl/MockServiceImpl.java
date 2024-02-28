package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.MockVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.exam.domain.po.Mock;
import com.zch.exam.mapper.MockMapper;
import com.zch.exam.service.IMockService;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MockServiceImpl extends ServiceImpl<MockMapper, Mock> implements IMockService {

    private final ITagsService tagsService;

    @Override
    public Page<MockVO> getMockPage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        Page<MockVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Mock::getTitle, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(Mock::getCategoryId, categoryId);
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(order), Mock::getId);
        Page<Mock> page = page(new Page<Mock>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        List<Mock> records = page.getRecords();
        List<MockVO> list = new ArrayList<>(records.size());
        for (Mock item : records) {
            MockVO vo1 = new MockVO();
            BeanUtils.copyProperties(item, vo1);
            CTagsVO tag = tagsService.getSimpleTagById(item.getCategoryId(), "PAPERS");
            if (ObjectUtils.isNull(tag)) {
                vo1.setCategory(null);
            }
            vo1.setCategory(tag);
            list.add(vo1);
        }
        vo.setTotal(count);
        vo.setRecords(list);
        return vo;
    }

    @Override
    public MockVO getMockById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new MockVO();
        }
        Mock mock = getById(id);
        if (ObjectUtils.isNull(mock)) {
            return new MockVO();
        }
        MockVO vo = new MockVO();
        BeanUtils.copyProperties(mock, vo);
        CTagsVO tag = tagsService.getSimpleTagById(mock.getCategoryId(), "PAPERS");
        if (ObjectUtils.isNull(tag)) {
            vo.setCategory(null);
        }
        vo.setCategory(tag);
        return vo;
    }

    @Override
    public List<CTagsVO> getTagList(List<Integer> ids) {
        if (ObjectUtils.isNull(ids) || CollUtils.isEmpty(ids)) {
            List<CTagsVO> list = tagsService.getSTagsList("PAPERS");
            if (ObjectUtils.isNull(list)) {
                return new ArrayList<>(0);
            }
            return list;
        }
        return null;
    }
}
