package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.PaperAndCategoryVO;
import com.zch.api.vo.exam.PapersVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.api.vo.exam.paper.PaperFrontVO;
import com.zch.api.vo.exam.practice.CategoryFirstVO;
import com.zch.api.vo.exam.practice.CategorySecondVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.exam.domain.po.Papers;
import com.zch.exam.mapper.PapersMapper;
import com.zch.exam.service.IPapersService;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {

    private final PapersMapper papersMapper;

    private final ITagsService tagsService;

    @Override
    public PaperAndCategoryVO getPapersPage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        PaperAndCategoryVO vo = new PaperAndCategoryVO();
        long count = count();
        if (count == 0) {
            vo.setCategories(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Papers> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Papers::getTitle, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(Papers::getCategoryId, categoryId);
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(sort), Papers::getId);
        Page<Papers> page = page(new Page<Papers>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setCategories(new ArrayList<>(0));
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<Papers> records = page.getRecords();
        List<PapersVO> list = new ArrayList<>(records.size());
        for (Papers item : records) {
            PapersVO vo1 = new PapersVO();
            BeanUtils.copyProperties(item, vo1);
            CTagsVO tag = tagsService.getSimpleTagById(item.getCategoryId(), "PAPERS");
            if (ObjectUtils.isNull(tag)) {
                vo1.setCategory(null);
            }
            vo1.setCategory(tag);
            list.add(vo1);
        }
        List<CTagsVO> tagsList = tagsService.getSTagsList("PAPERS");
        vo.getData().setTotal(count);
        vo.getData().setData(list);
        vo.setCategories(tagsList);
        return vo;
    }

    @Override
    public PapersVO getPaperById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new PapersVO();
        }
        Papers papers = papersMapper.selectById(id);
        if (ObjectUtils.isNull(papers)) {
            return new PapersVO();
        }
        PapersVO vo = new PapersVO();
        BeanUtils.copyProperties(papers, vo);
        CTagsVO tag = tagsService.getSimpleTagById(papers.getCategoryId(), "PAPERS");
        if (ObjectUtils.isNull(tag)) {
            vo.setCategory(null);
        }
        vo.setCategory(tag);
        return vo;
    }

    @Override
    public Boolean deletePaperById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public List<CTagsVO> getCategoryList() {
        List<CTagsVO> list = tagsService.getSTagsList("PAPERS");
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list;
    }

    @Override
    public Page<TagsVO> getTagList(Integer pageNum, Integer pageSize) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        return tagsService.getCategoryList(pageNum, pageSize, "PAPERS");
    }

    @Override
    public CTagsVO getTagById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new CTagsVO();
        }
        return tagsService.getSimpleTagById(id, "PAPERS");
    }

    @Override
    public Boolean addTag(TagForm form) {
        return tagsService.addTag(form);
    }

    @Override
    public Boolean updateTag(Integer id, TagForm form) {
        return tagsService.updateTag(id, form);
    }

    @Override
    public Boolean deleteTag(Integer id) {
        return tagsService.deleteTag(id, "PAPERS");
    }

    @Override
    public long getPaperCount() {
        return count();
    }

    @Override
    public PaperFrontVO getPaperList(Integer pageNum, Integer pageSize, Integer categoryId, Integer childId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
                || ObjectUtils.isNull(categoryId) || ObjectUtils.isNull(childId)) {
            pageNum = 1;
            pageSize = 10;
            categoryId = 0;
            childId = 0;
        }
        PaperFrontVO vo = new PaperFrontVO();
        // 查找分类
        // 一级分类
        List<CategoryFirstVO> categories = tagsService.getFirstCategoryList("PAPERS");
        if (ObjectUtils.isNotNull(categories) && CollUtils.isNotEmpty(categories)) {
            vo.setCategories(categories);
        }
        // 构造二级分类
        Map<Integer, List<CategorySecondVO>> childrenCategories = new HashMap<>();
        if (CollUtils.isNotEmpty(categories)) {
            for (CategoryFirstVO item : categories) {
                // 二级分类
                List<CategorySecondVO> children = tagsService.getSecondCategoryList(item.getId(), "PAPERS");
                if (ObjectUtils.isNotNull(children) && CollUtils.isNotEmpty(children)) {
                    childrenCategories.put(item.getId(), children);
                }
            }
            vo.setChildrenCategories(childrenCategories);
        }
        // 查找练习数量
        long count = count();
        if (count == 0) {
            vo.getData().setData(new ArrayList<>(0));
            vo.getData().setTotal(0);
            return vo;
        }
        LambdaQueryWrapper<Papers> wrapper = new LambdaQueryWrapper<>();
        /*
            1. categoryId != 0 && childId == 0
            2. categoryId != 0 && childId != 0
            3. categoryId == 0 && childId == 0
         */
        if ((! Objects.equals(categoryId, 0)) && Objects.equals(childId, 0)) {
            // 这里 一级分类 查询有一个坑，不能单纯等于一级分类，因为可能某个练习的分类是一级分类下的二级分类
            // 应该将该一级分类下的所有二级分类查出来，查询就在一级和二级分类id列表中
            if (! childrenCategories.isEmpty()) {
                List<CategorySecondVO> list = childrenCategories.get(categoryId);
                if (CollUtils.isNotEmpty(list)) {
                    // 有二级分类的情况下
                    List<Integer> ids = list.stream().map(CategorySecondVO::getId).collect(Collectors.toList());
                    // 将一级分类id放入
                    ids.add(categoryId);
                    // 则查询条件在这个分类id列表中
                    wrapper.in(Papers::getCategoryId, ids);
                } else {
                    // 没有二级分类情况下，直接使用一级分类
                    wrapper.eq(Papers::getCategoryId, categoryId);
                }
            } else {
                // 都没有二级分类，直接使用一级分类
                wrapper.eq(Papers::getCategoryId, categoryId);
            }
        } else if ((! Objects.equals(categoryId, 0)) && (! Objects.equals(childId, 0))) {
            // 第二种情况时，既然都有二级分类id，那么就是精准查询，直接使用二级分类id即可
            wrapper.eq(Papers::getCategoryId, childId);
        }
        Page<Papers> page = page(new Page<Papers>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<PapersVO> list = page.getRecords().stream().map(item -> {
            PapersVO vo1 = new PapersVO();
            BeanUtils.copyProperties(item, vo1);
            CTagsVO tag = tagsService.getSimpleTagById(item.getId(), "PAPERS");
            if (ObjectUtils.isNotNull(tag)) {
                vo1.setCategory(tag);
            }
            return vo1;
        }).collect(Collectors.toList());
        vo.getData().setData(list);
        vo.getData().setTotal(count);
        return vo;
    }
}
