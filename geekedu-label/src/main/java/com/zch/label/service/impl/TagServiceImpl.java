package com.zch.label.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.common.exceptions.CommonException;
import com.zch.common.utils.CollUtils;
import com.zch.common.utils.IdUtils;
import com.zch.common.utils.StringUtils;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.CategoryTag;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.mapper.CategoryTagMapper;
import com.zch.label.mapper.TagMapper;
import com.zch.label.service.ITagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements ITagService {

    private final TagMapper tagMapper;

    private final CategoryMapper categoryMapper;

    private final CategoryTagMapper categoryTagMapper;

    @Override
    public PageVO<TagDTO> getTagList(PageReqVO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<TagDTO> result = tagMapper.selectTagList(req).stream()
                .map(item -> TagDTO.of(item.getId(), item.getName()))
                .collect(Collectors.toList());
        PageInfo<TagDTO> pageInfo = new PageInfo<>(result);
        PageVO<TagDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(req.getPageNum());
        vo.setPageSize(req.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return vo;
    }

    @Override
    public PageVO<TagDTO> getTagByCondition(CategoryTagQuery query) {
        if (StringUtils.isBlank(query.getTagName())) {
            throw new CommonException("请输入标签名！");
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<TagDTO> result = tagMapper.selectTagByCondition(query).stream()
                .map(item -> TagDTO.of(item.getId(), item.getName()))
                .collect(Collectors.toList());
        PageInfo<TagDTO> pageInfo = new PageInfo<>(result);
        PageVO<TagDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(query.getPageNum());
        vo.setPageSize(query.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return vo;
    }

    @Override
    public Tag addTag(TagForm form) {
        if (form.getCategoryId() == null || StringUtils.isBlank(form.getName())) {
            throw new CommonException("请选择对应分类下或者输入更改后的标签名！");
        }
        Date time = new Date();
        // 构建数据
        Tag tag = new Tag();
        tag.setId(IdUtils.getId());
        tag.setName(form.getName());
        tag.setCreatedBy(1484844949L);
        tag.setCreatedTime(time);
        tag.setUpdatedBy(1484844949L);
        tag.setUpdatedTime(time);

        // 从数据库中查找，同一分类下不能存在相同标签
        List<CategoryTag> categoryTags = categoryTagMapper.selectTagByCategoryId(form.getCategoryId());
        boolean isSameName = false;
        // 如果不为空，则需要继续判断是否有重名标签
        if (CollUtils.isNotEmpty(categoryTags)) {
            List<Tag> tagList = new ArrayList<>();
            for (CategoryTag item : categoryTags) {
                tagList.add(tagMapper.selectTagById(item.getTagId()));
            }
            // 和新传入的tagName进行对比，若有相同的则直接退出
            for (Tag item : tagList) {
                if (StringUtils.isSameStringByUpperToLower(item.getName(), form.getName())) {
                    // 说明有重名的标签名
                    isSameName = true;
                    break;
                }
            }
        }
        if (isSameName) {
            throw new CommonException("不允许同一分类下有相同标签名！");
        }

        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setId(IdUtils.getId());
        categoryTag.setCategoryId(form.getCategoryId());
        categoryTag.setTagId(tag.getId());
        categoryTag.setCreatedBy(1484844949L);
        categoryTag.setCreatedTime(time);
        categoryTag.setUpdatedBy(1484844949L);
        categoryTag.setUpdatedTime(time);
        int row = tagMapper.insertTag(tag);
        int row1 = categoryTagMapper.insertCategoryTag(categoryTag);
        if (row != 1 || row1 != 1) {
            throw new CommonException("服务器新增标签错误，请联系管理员！");
        }
        return tag;
    }

    @Override
    public Tag deleteTag(Long id) {
        if (id == null) {
            throw new CommonException("请选择要删除的标签！");
        }
        Date time = new Date();
        Tag tag = new Tag();
        tag.setId(id);
        tag.setUpdatedBy(1484844949L);
        tag.setUpdatedTime(time);
        int row = tagMapper.deleteTag(tag);
        if (row != 1) {
            throw new CommonException("服务器删除标签错误，请联系管理员！");
        }
        return tag;
    }

    @Override
    public Tag updateTag(TagForm form) {
        if (form.getId() == null || StringUtils.isBlank(form.getName())) {
            throw new CommonException("请选择需要更改的标签或输入更改后的标签名！");
        }
        Date time = new Date();
        Tag tag = new Tag();
        tag.setId(form.getId());
        tag.setName(form.getName());
        tag.setCreatedBy(1484844949L);
        tag.setCreatedTime(time);
        tag.setUpdatedBy(1484844949L);
        tag.setUpdatedTime(time);
        int row = tagMapper.updateTag(tag);
        if (row != 1) {
            return new Tag();
        }
        // 查询出来最新的 tag 返回给前端
        Tag res = tagMapper.selectTagById(form.getId());
        if (res == null) {
            throw new CommonException("未找到对应标签，请输入正确的标签名！");
        }
        return res;
    }
}
