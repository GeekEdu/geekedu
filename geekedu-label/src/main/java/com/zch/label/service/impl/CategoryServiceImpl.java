package com.zch.label.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.label.CategoryForm;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.exception.DbException;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.po.CategoryTag;
import com.zch.label.domain.po.Tag;
import com.zch.label.enums.CategoryEnum;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.mapper.CategoryTagMapper;
import com.zch.label.mapper.TagMapper;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.zch.common.core.constants.ErrorInfo.Msg.*;


/**
 * @author Poison02
 * @date 2024/1/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final CategoryTagMapper categoryTagMapper;

    @Override
    public Boolean addCategory(CategoryForm form) {
        if (StringUtils.isBlank(form.getName()) || ObjectUtils.isNull(form.getType())) {
            throw new CommonException("请传入正确的分类名和类型！");
        }
        // 没有传父级id就直接存 category 否则存 category_tag 和 tag
        if (ObjectUtils.isNull(form.getCategoryId())) {
            // 先去数据库中查询，若存在则报错
            Category one = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .eq(Category::getName, form.getName())
                    .eq(Category::getType, CategoryEnum.valueOf(form.getType()))
                    .select(Category::getName, Category::getType));
            if (ObjectUtils.isNotNull(one)) {
                throw new DbException(DATE_SELECT_IS_EXISTS);
            }
            // 组装数据存库
            Category category = Category.builder()
                    .name(form.getName())
                    .type(CategoryEnum.valueOf(form.getType()))
                    .createdBy(1745747394693820416L)
                    .updatedBy(1745747394693820416L)
                    .build();
            int row = categoryMapper.insert(category);
            if (row != 1) {
                throw new DbException(DB_SAVE_EXCEPTION);
            }
        } else {
            Tag two = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getName, form.getName())
                    .eq(Tag::getType, CategoryEnum.valueOf(form.getType()))
                    .select(Tag::getName, Tag::getType));
            if (ObjectUtils.isNotNull(two)) {
                throw new DbException(DATE_SELECT_IS_EXISTS);
            }
            Tag tag = Tag.builder()
                    .name(form.getName())
                    .type(CategoryEnum.valueOf(form.getType()))
                    .createdBy(1745747394693820416L)
                    .updatedBy(1745747394693820416L)
                    .build();
            int row1 = tagMapper.insert(tag);
            if (row1 != 1) {
                throw new DbException(DB_SAVE_EXCEPTION);
            }
            // 将 tag 的id查出来
            Tag tagSelect = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getName, form.getName())
                    .eq(Tag::getType, CategoryEnum.valueOf(form.getType()))
                    .select(Tag::getId, Tag::getName, Tag::getType));
            CategoryTag categoryTag = CategoryTag.builder()
                    .categoryId(form.getCategoryId())
                    .tagId(tagSelect.getId())
                    .type(CategoryEnum.valueOf(form.getType()))
                    .createdBy(1745747394693820416L)
                    .updatedBy(1745747394693820416L)
                    .build();
            categoryTagMapper.insert(categoryTag);
        }
        return true;
    }
}
