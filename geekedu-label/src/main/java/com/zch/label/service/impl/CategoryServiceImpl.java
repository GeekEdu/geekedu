package com.zch.label.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.api.vo.label.TagVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.exception.DbException;
import com.zch.common.satoken.context.UserContext;
import com.zch.label.domain.po.Category;
import com.zch.label.enums.CategoryEnum;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Boolean addCategory(CategoryForm form) {
        if (StringUtils.isBlank(form.getName()) || ObjectUtils.isNull(form.getType())) {
            throw new CommonException("请传入正确的分类名和类型！");
        }
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
                .parentId(form.getParentId())
                .createdBy(UserContext.getLoginId())
                .updatedBy(UserContext.getLoginId())
                .sort(form.getSort())
                .build();
        int row = categoryMapper.insert(category);
        if (row != 1) {
            throw new DbException(DB_SAVE_EXCEPTION);
        }
        return true;
    }

    @Override
    public Page<CategoryVO> getCategoryPage(Integer pageNum, Integer pageSize, String type) {
        // 查出一级分类
        Page<Category> page = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Category>()
                        .select(Category::getId, Category::getSort, Category::getName, Category::getType, Category::getParentId)
                        .eq(Category::getIsDelete, 0)
                        .eq(Category::getParentId, 0)
                        .eq(Category::getType, CategoryEnum.valueOf(type)));
        Page<CategoryVO> result = new Page<>();
        List<Category> category = page.getRecords();
        if (CollUtils.isEmpty(category)) {
            return new Page<>();
        }
        List<Category> records = page.getRecords();
        for (Category item : records) {
            // 查找二级分类，二级分类的 parentId == 父级分类的id
            List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .select(Category::getId, Category::getSort, Category::getName, Category::getType, Category::getParentId)
                    .eq(Category::getIsDelete, 0)
                    .eq(Category::getParentId, item.getId())
                    .eq(Category::getType, CategoryEnum.valueOf(type)));
            if (CollUtils.isEmpty(categories)) {
                item.setChildren(new ArrayList<>(0));
            }
            // 将二级分类转为 TagVO
            List<TagVO> children = categories.stream().map(e -> {
                TagVO vo = new TagVO();
                vo.setId(e.getId());
                vo.setName(e.getName());
                vo.setSort(e.getSort());
                vo.setParentId(item.getId());
                return vo;
            }).collect(Collectors.toList());
            // 将二级分类放入结果集中
            item.setChildren(children);
        }
        BeanUtils.copyProperties(page, result);
        List<CategoryVO> vos = category.stream().map(item -> {
            CategoryVO vo = new CategoryVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setSort(item.getSort());
            vo.setParentId(item.getParentId());
            vo.setChildren(item.getChildren());
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(vos);
        return result;
    }

    @Override
    public CategorySimpleVO getCategoryById(Integer id, String type) {
        if (ObjectUtils.isNull(id) || ObjectUtils.isNull(type)) {
            throw new CommonException("请输入正确的分类id或分类类型！");
        }
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getId, id)
                .eq(Category::getType, CategoryEnum.valueOf(type))
                .eq(Category::getIsDelete, 0)
                .select(Category::getId, Category::getName, Category::getSort, Category::getParentId));
        if (ObjectUtils.isNull(category)) {
            return new CategorySimpleVO();
        }
        CategorySimpleVO vo = new CategorySimpleVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setSort(category.getSort());
        vo.setParentId(category.getParentId());
        return vo;
    }

    @Override
    public List<CategoryVO> getCategory(String type) {
        if (ObjectUtils.isNull(type)) {
            throw new CommonException("请输入正确的分类类型！");
        }
        // 查询一级分类
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getSort, Category::getName, Category::getType, Category::getParentId)
                .eq(Category::getIsDelete, 0)
                .eq(Category::getParentId, 0)
                .eq(Category::getType, CategoryEnum.valueOf(type))
                .orderByAsc(Category::getSort));
        if (CollUtils.isEmpty(categories)) {
            return new ArrayList<>(0);
        }
        for (Category item : categories) {
            // 查找二级分类，二级分类的 parentId == 父级分类的id
            List<Category> secondCategories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                    .select(Category::getId, Category::getSort, Category::getName, Category::getType, Category::getParentId)
                    .eq(Category::getIsDelete, 0)
                    .eq(Category::getParentId, item.getId())
                    .eq(Category::getType, CategoryEnum.valueOf(type))
                    .orderByAsc(Category::getSort));
            if (CollUtils.isEmpty(secondCategories)) {
                item.setChildren(new ArrayList<>(0));
            }
            // 将二级分类转为 TagVO
            List<TagVO> children = secondCategories.stream().map(e -> {
                TagVO vo = new TagVO();
                vo.setId(e.getId());
                vo.setName(e.getName());
                vo.setSort(e.getSort());
                vo.setParentId(item.getId());
                return vo;
            }).collect(Collectors.toList());
            // 将二级分类放入结果集中
            item.setChildren(children);
        }
        List<CategoryVO> vos = categories.stream().map(item -> {
            CategoryVO vo = new CategoryVO();
            vo.setId(item.getId());
            vo.setName(item.getName());
            vo.setSort(item.getSort());
            vo.setParentId(item.getParentId());
            vo.setChildren(item.getChildren());
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public List<CategorySimpleVO> getCategorySimpleList(String type) {
        if (StringUtils.isBlank(type)) {
            return new ArrayList<>(0);
        }
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getSort, Category::getName, Category::getType, Category::getParentId)
                .eq(Category::getType, CategoryEnum.valueOf(type))
                .orderByAsc(Category::getSort));
        if (CollUtils.isEmpty(categories)) {
            return new ArrayList<>(0);
        }
        List<CategorySimpleVO> vos = categories.stream().map(item -> {
            CategorySimpleVO vo = new CategorySimpleVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public Boolean updateCategory(Integer id, CategoryForm form) {
        // 获取用户id
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        if (ObjectUtils.isNull(id)) {
            throw new CommonException("请传入正确的分类id！");
        }
        // 先查看修改后的分类名是否存在过，存在过则不能修改
        Category one = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getName, form.getName())
                .eq(Category::getIsDelete, 0)
                .eq(Category::getSort, form.getSort())
                .eq(Category::getType, CategoryEnum.valueOf(form.getType()))
                .eq(Category::getParentId, form.getParentId()));
        if (ObjectUtils.isNotNull(one)) {
            throw new CommonException(DATE_SELECT_IS_EXISTS);
        }
        // 更新分类
        Category category = Category.builder()
                .id(id)
                .name(form.getName())
                .parentId(form.getParentId())
                .sort(form.getSort())
                .updatedBy(userId)
                .build();
        int row = categoryMapper.updateById(category);
        if (row != 1) {
            throw new DbException(DB_UPDATE_EXCEPTION);
        }
        return true;
    }

    @Override
    public Boolean deleteCategory(Integer id) {
        if (ObjectUtils.isNull(id)) {
            throw new CommonException("请传入正确的分类id！");
        }
        int row = categoryMapper.deleteById(id);
        if (row != 1) {
            throw new DbException(DB_DELETE_EXCEPTION);
        }
        return true;
    }
}
