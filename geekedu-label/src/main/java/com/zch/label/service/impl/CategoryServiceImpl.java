package com.zch.label.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zch.api.dto.label.CategoryForm;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.common.exceptions.CommonException;
import com.zch.common.utils.CollUtils;
import com.zch.common.utils.IdUtils;
import com.zch.common.utils.ObjectUtils;
import com.zch.common.utils.StringUtils;
import com.zch.label.domain.dto.CategoryDTO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.po.CategoryTag;
import com.zch.label.domain.po.Tag;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryReqVO;
import com.zch.label.enums.CategoryEnum;
import com.zch.label.mapper.CategoryMapper;
import com.zch.label.mapper.CategoryTagMapper;
import com.zch.label.mapper.TagMapper;
import com.zch.label.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryMapper categoryMapper;

    private final CategoryTagMapper categoryTagMapper;

    private final TagMapper tagMapper;

    @Override
    public PageVO<CategoryDTO> getCategoryList(PageReqVO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<CategoryDTO> result = categoryMapper.selectCategoryList().stream()
                .map(item -> CategoryDTO.of(item.getId(), item.getName(), item.getType()))
                .collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(result);
        PageVO<CategoryDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(req.getPageNum());
        vo.setPageSize(req.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return vo;
    }

    @Override
    public PageVO<CategoryDTO> getCategoryByCondition(CategoryTagQuery query) {
        if (StringUtils.isBlank(query.getCategoryName())) {
            throw new CommonException("请输入分类名！");
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<CategoryDTO> result = categoryMapper.selectCategoryByCondition(query).stream()
                .map(item -> CategoryDTO.of(item.getId(), item.getName(), item.getType()))
                .collect(Collectors.toList());
        PageInfo<CategoryDTO> pageInfo = new PageInfo<>(result);
        PageVO<CategoryDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(query.getPageNum());
        vo.setPageSize(query.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return vo;
    }

    @Override
    public Category addCategory(CategoryForm form) {
        if (StringUtils.isBlank(form.getName()) || StringUtils.isBlank(form.getType())) {
            throw new CommonException("请选择对应类型或者输入分类名！");
        }
        // 查询某一类型下是否有重名分类
        Short type = CategoryEnum.returnCode(form.getType());
        Date time = new Date();
        if (type < 0) {
            throw new CommonException("请输入正确的分类类型！");
        }
        Category res = categoryMapper.selectCategoryByNameAndType(form.getName(), type);
        if (ObjectUtils.isNotNull(res)) {
            throw new CommonException("不允许同一类型下有重名分类！");
        }
        // 构建数据并添加进数据库
        Category category = new Category();
        category.setId(IdUtils.getId());
        category.setName(form.getName());
        category.setType(type);
        category.setCreatedBy(1484844949L);
        category.setCreatedTime(time);
        category.setUpdatedBy(1484844949L);
        category.setUpdatedTime(time);
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setId(IdUtils.getId());
        categoryTag.setCategoryId(category.getId());
        categoryTag.setCreatedBy(1484844949L);
        categoryTag.setCreatedTime(time);
        categoryTag.setUpdatedBy(1484844949L);
        categoryTag.setUpdatedTime(time);
        int row = categoryMapper.insertCategory(category);
        int row1 = categoryTagMapper.insertCategoryTag(categoryTag);
        if (row != 1 || row1 != 1) {
            throw new CommonException("服务器新增分类错误，请联系管理员！");
        }
        return category;
    }

    @Override
    public Category deleteCategory(Long id) {
        if (id == null) {
            throw new CommonException("请选择要删除的分类！");
        }
        Date time = new Date();
        Category category = new Category();
        category.setId(id);
        category.setUpdatedBy(1484844949L);
        category.setUpdatedTime(time);
        CategoryTag categoryTag = new CategoryTag();
        categoryTag.setCategoryId(id);
        categoryTag.setUpdatedBy(1484844949L);
        categoryTag.setUpdatedTime(time);
        // 删除 category_tag 表里面对应的分类，则需要将该分类下所有标签都删除
        List<CategoryTag> categoryTags = categoryTagMapper.selectTagByCategoryId(id);
        if (CollUtils.isNotEmpty(categoryTags)) {
            // 过滤掉 tagId 为空的数据
            List<CategoryTag> list = categoryTags.stream()
                    .filter(item -> item.getTagId() != null)
                    .toList();
            if (CollUtils.isNotEmpty(list)) {
                List<Tag> tagList = new ArrayList<>();
                for (CategoryTag item : list) {
                    tagList.add(tagMapper.selectTagById(item.getTagId()));
                }
                if (CollUtils.isNotEmpty(tagList)) {
                    List<Long> ids = tagList.stream()
                            .map(Tag::getId).toList();
                    // 批量删除标签
                    tagMapper.deleteTagBatch(ids);
                }
            }
        }
        int row = categoryMapper.deleteCategory(category);
        int row1 = categoryTagMapper.deleteCategory(categoryTag);
        if (row != 1 || row1 != 1) {
            throw new CommonException("服务器删除分类错误，请联系管理员！");
        }
        return category;
    }

    @Override
    public Category updateCategory(CategoryForm form) {
        if (form.getId() == null || StringUtils.isBlank(form.getName())) {
            throw new CommonException("请选择需要更改的标签或输入更改后的标签名！");
        }
        short type = CategoryEnum.returnCode(form.getType());
        if (type < 0) {
            throw new CommonException("请选择分类类型或输入更改后的分类类型！");
        }
        Date time = new Date();
        Category category = new Category();
        category.setId(form.getId());
        category.setName(form.getName());
        category.setType(type);
        category.setCreatedBy(1484844949L);
        category.setCreatedTime(time);
        category.setUpdatedBy(1484844949L);
        category.setUpdatedTime(time);
        int row = categoryMapper.updateCategory(category);
        if (row != 1) {
            throw new CommonException("服务器更新分类出错，请联系管理员！");
        }
        // 查询出来最新的 tag 返回给前端
        Category res = categoryMapper.selectCategoryById(form.getId());
        if (res == null) {
            throw new CommonException("未找到对应对应，请输入正确的分类名！");
        }
        return res;
    }

    @Override
    public PageVO<TagDTO> getTagsCategoryId(CategoryReqVO req) {
        if (req.getId() == null) {
            throw new CommonException("请输入或选择正确的分类！");
        }
        List<CategoryTag> categoryTags = categoryTagMapper.selectTagByCategoryId(req.getId());
        if (CollUtils.isEmpty(categoryTags)) {
            throw new CommonException("该分类下没有任何标签！");
        }
        List<Long> ids = categoryTags.stream()
                .map(CategoryTag::getTagId)
                .toList();
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        req.setIds(ids);
        List<TagDTO> result = tagMapper.selectTagAll(req).stream()
                .map(item -> TagDTO.of(item.getId(), item.getName()))
                .toList();
        PageInfo pageInfo = new PageInfo(result);
        PageVO<TagDTO> vo = new PageVO<>();
        vo.setTotal(pageInfo.getTotal());
        vo.setPageNum(req.getPageNum());
        vo.setPageSize(req.getPageSize());
        vo.setPageCount(pageInfo.getPages());
        vo.setList(result);
        return vo;
    }
}
