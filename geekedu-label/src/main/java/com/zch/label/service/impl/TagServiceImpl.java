package com.zch.label.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.api.dto.label.TagForm;
import com.zch.common.domain.Response;
import com.zch.common.domain.query.PageQuery;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.common.utils.IdUtils;
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
    public Response<PageVO<TagDTO>> getTagList(PageReqVO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<TagDTO> result = tagMapper.selectTagList(req).stream()
                .map(item -> {
                    TagDTO tagDTO = new TagDTO();
                    tagDTO.setId(item.getId());
                    tagDTO.setName(item.getName());
                    return tagDTO;
                }).collect(Collectors.toList());
        PageInfo<TagDTO> pageInfo = new PageInfo<>(result);
        PageVO<TagDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(req.getPageNum());
        vo.setPageSize(req.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return Response.ok(vo);
    }

    @Override
    public Response<PageQuery> getTagByCondition(CategoryTagQuery query) {
        List<TagDTO> result = tagMapper.selectTagByCondition(query).stream()
                .map(item -> {
                    TagDTO tagDTO = new TagDTO();
                    tagDTO.setId(item.getId());
                    tagDTO.setName(item.getName());
                    return tagDTO;
                }).collect(Collectors.toList());
        return Response.ok(query.getPageQuery(query, result));
    }

    @Override
    public Response<Tag> addTag(TagForm form) {
        // 构建数据，添加到 tag 表中
        Date time = new Date();
        Tag tag = new Tag();
        tag.setId(IdUtils.getId());
        tag.setName(form.getName());
        tag.setCreatedBy(1484844949L);
        tag.setCreatedTime(time);
        tag.setUpdatedBy(1484844949L);
        tag.setUpdatedTime(time);
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
            return Response.error("新增标签失败！请联系管理员！");
        }
        return Response.ok(tag);
    }

    @Override
    public Response<Tag> deleteTag(Long id) {
        return null;
    }

    @Override
    public Response<Tag> updateTag(TagForm form) {
        return null;
    }
}
