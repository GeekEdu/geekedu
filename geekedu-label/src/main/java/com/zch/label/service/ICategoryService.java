package com.zch.label.service;

import com.zch.api.dto.label.CategoryForm;
import com.zch.common.domain.vo.PageReqVO;
import com.zch.common.domain.vo.PageVO;
import com.zch.label.domain.dto.CategoryDTO;
import com.zch.label.domain.dto.TagDTO;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.query.CategoryTagQuery;
import com.zch.label.domain.vo.CategoryReqVO;

/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ICategoryService {

    /**
     * 查找所有分类
     * @param req
     * @return
     */
    PageVO<CategoryDTO> getCategoryList(PageReqVO req);

    /**
     * 条件查询分类
     * @param query
     * @return
     */
    PageVO<CategoryDTO> getCategoryByCondition(CategoryTagQuery query);

    /**
     * 新增分类
     * @param form
     * @return
     */
    Category addCategory(CategoryForm form);

    /**
     * 删除分类
     * @param id
     * @return
     */
    Category deleteCategory(Long id);

    /**
     * 更新分类
     * @param form
     * @return
     */
    Category updateCategory(CategoryForm form);

    /**
     * 查找某一分类下的所有标签
     * @param req
     * @return
     */
    PageVO<TagDTO> getTagsCategoryId(CategoryReqVO req);

}
