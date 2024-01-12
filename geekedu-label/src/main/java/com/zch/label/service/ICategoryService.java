package com.zch.label.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.label.CategoryForm;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryPageVO;
import com.zch.label.domain.vo.TagPageVO;

/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 查找所有分类
     * @param query
     * @return
     */
    Page<CategoryPageVO> getCategoryList(CategoryTagQuery query);

    /**
     * 条件查询分类
     * @param query
     * @return
     */
    Page<CategoryPageVO> getCategoryByCondition(CategoryTagQuery query);

    /**
     * 新增分类
     * @param form
     * @return
     */
    boolean addCategory(CategoryForm form);

    /**
     * 删除分类
     * @param id
     * @return
     */
    boolean deleteCategory(Long id);

    /**
     * 更新分类
     * @param form
     * @return
     */
    Category updateCategory(CategoryForm form);

    /**
     * 查找某一分类下的所有标签
     * @param query
     * @return
     */
    Page<TagPageVO> getTagsCategoryId(CategoryTagQuery query);

}
