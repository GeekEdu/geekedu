package com.zch.label.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.label.TagForm;
import com.zch.common.exceptions.CommonException;
import com.zch.common.exceptions.DbException;
import com.zch.common.utils.BeanUtils;
import com.zch.common.utils.CollUtils;
import com.zch.common.utils.IdUtils;
import com.zch.common.utils.StringUtils;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.po.CategoryTag;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.TagPageVO;
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

import static com.zch.common.constants.ErrorInfo.Msg.*;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final TagMapper tagMapper;

    private final CategoryMapper categoryMapper;

    private final CategoryTagMapper categoryTagMapper;

    @Override
    public Page<TagPageVO> getTagList(CategoryTagQuery query) {
        // 取出查询参数
        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();

        // 查询数据
        Page<Tag> page = this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Tag>()
                        .select(Tag::getId, Tag::getName)
                        .eq(Tag::getIsDelete, 0)
        );
        Page<TagPageVO> result = new Page<>();
        BeanUtils.copyProperties(page, result);
        return result;
    }

    @Override
    public Page<TagPageVO> getTagByCondition(CategoryTagQuery query) {
        if (StringUtils.isBlank(query.getTagName())) {
            throw new CommonException("请输入标签名！");
        }
        // 取出查询参数
        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();
        String tagName = query.getTagName();

        // 查询数据
        Page<Tag> page = this.page(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Tag>()
                        .like(StringUtils.isNotBlank(tagName), Tag::getName, tagName)
                        .eq(Tag::getIsDelete, 0)
                        .select(Tag::getId, Tag::getName)

        );
        Page<TagPageVO> result = new Page<>();
        BeanUtils.copyProperties(page, result);
        return result;
    }

    @Override
    public boolean addTag(TagForm form) {
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
        // 查询该标签的类型
        Category category = categoryMapper.selectCategoryById(form.getCategoryId());

        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setId(IdUtils.getId());
        categoryTag.setCategoryId(form.getCategoryId());
        categoryTag.setTagId(tag.getId());
        categoryTag.setType(category.getType());
        categoryTag.setCreatedBy(1484844949L);
        categoryTag.setCreatedTime(time);
        categoryTag.setUpdatedBy(1484844949L);
        categoryTag.setUpdatedTime(time);
        int row = tagMapper.insertTag(tag);
        int row1 = categoryTagMapper.insertCategoryTag(categoryTag);
        if (row != 1 || row1 != 1) {
            throw new DbException(DB_SAVE_EXCEPTION);
        }
        return true;
    }

    @Override
    public boolean deleteTag(Long id) {
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
            throw new DbException(DB_DELETE_EXCEPTION);
        }
        return true;
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
            throw new DbException(DB_UPDATE_EXCEPTION);
        }
        return res;
    }
}
