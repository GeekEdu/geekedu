package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.exam.domain.po.Tags;
import com.zch.exam.enums.ExamTagsEnum;
import com.zch.exam.mapper.TagsMapper;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

    private final TagsMapper tagsMapper;

    @Override
    public List<TagsVO> getTagsList(String type) {
        List<Tags> tags = tagsMapper.selectList(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getType, ExamTagsEnum.valueOf(type)));
        if (ObjectUtils.isNull(tags) || CollUtils.isEmpty(tags)) {
            return new ArrayList<>(0);
        }
        List<TagsVO> vos = tags.stream().map(item -> {
            TagsVO vo = new TagsVO();
            BeanUtils.copyProperties(item, vo);
            // 查找子分类
            List<Tags> list = tagsMapper.selectList(new LambdaQueryWrapper<Tags>()
                    .eq(Tags::getType, type)
                    .eq(Tags::getParentId, item.getId()));
            if (ObjectUtils.isNull(list)) {
                vo.setChildren(new ArrayList<>(0));
            }
            List<CTagsVO> children = list.stream().map(e -> {
                CTagsVO cTag = new CTagsVO();
                BeanUtils.copyProperties(e, cTag);
                return cTag;
            }).collect(Collectors.toList());
            vo.setChildren(children);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public TagsVO getTagByCondition(Integer id, String type) {
        Tags tags = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getId, id)
                .eq(Tags::getType, ExamTagsEnum.valueOf(type)));
        if (ObjectUtils.isNull(tags)) {
            return null;
        }
        TagsVO vo = new TagsVO();
        BeanUtils.copyProperties(tags, vo);
        // 查找子分类
        List<Tags> list = tagsMapper.selectList(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getType, type)
                .eq(Tags::getParentId, id));
        if (ObjectUtils.isNull(list)) {
            vo.setChildren(new ArrayList<>(0));
        }
        List<CTagsVO> children = list.stream().map(e -> {
            CTagsVO cTag = new CTagsVO();
            BeanUtils.copyProperties(e, cTag);
            return cTag;
        }).collect(Collectors.toList());
        vo.setChildren(children);
        return vo;
    }

    @Override
    public Page<TagsVO> getCategoryList(Integer pageNum, Integer pageSize, String type) {
        Page<TagsVO> vo = new Page<>();
        Page<Tags> page = page(new Page<Tags>(pageNum, pageSize), new LambdaQueryWrapper<Tags>()
                .eq(Tags::getType, ExamTagsEnum.valueOf(type))
                .eq(Tags::getParentId, 0));
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
        }
        List<Tags> records = page.getRecords();
        List<TagsVO> list = records.stream().map(item -> {
            TagsVO vo1 = new TagsVO();
            BeanUtils.copyProperties(item, vo1);
            // 查找子分类
            List<Tags> tags = tagsMapper.selectList(new LambdaQueryWrapper<Tags>()
                    .eq(Tags::getType, type)
                    .eq(Tags::getParentId, item.getId()));
            if (ObjectUtils.isNull(tags)) {
                vo1.setChildren(new ArrayList<>(0));
            }
            List<CTagsVO> children = tags.stream().map(e -> {
                CTagsVO cTag = new CTagsVO();
                BeanUtils.copyProperties(e, cTag);
                return cTag;
            }).collect(Collectors.toList());
            vo1.setChildren(children);
            return vo1;
        }).collect(Collectors.toList());
        vo.setRecords(list);
        vo.setTotal(page.getTotal());
        return vo;
    }

    @Override
    public Boolean deleteTag(Integer id, String type) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        Tags one = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getId, id)
                .eq(Tags::getType, ExamTagsEnum.valueOf(type)));
        if (ObjectUtils.isNull(one)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean updateTag(Integer id, TagForm form) {
        if (ObjectUtils.isNull(id) || ObjectUtils.isNull(form)) {
            return false;
        }
        Tags one = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getId, id)
                .eq(Tags::getType, ExamTagsEnum.valueOf(form.getType())));
        if (ObjectUtils.isNull(one)) {
            return false;
        }
        Tags tags = new Tags();
        tags.setId(id);
        tags.setName(form.getName());
        tags.setSort(form.getSort());
        tags.setParentId(form.getParentId());
        tags.setUpdatedBy(UserContext.getLoginId());
        return updateById(tags);
    }

    @Override
    public Boolean addTag(TagForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        Tags one = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>()
                .eq(Tags::getParentId, form.getParentId())
                .eq(Tags::getName, form.getName())
                .eq(Tags::getType, ExamTagsEnum.valueOf(form.getType())));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Tags tags = new Tags();
        tags.setName(form.getName());
        tags.setSort(form.getSort());
        tags.setParentId(form.getParentId());
        tags.setType(ExamTagsEnum.valueOf(form.getType()));
        tags.setCreatedBy(userId);
        tags.setUpdatedBy(userId);
        return save(tags);
    }
}
