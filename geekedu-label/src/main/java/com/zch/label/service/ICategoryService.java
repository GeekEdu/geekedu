package com.zch.label.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.dto.label.CategoryForm;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.label.CategoryVO;
import com.zch.label.domain.po.Category;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/7
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 新增分类
     * @param form
     * @return
     */
    Boolean addCategory(CategoryForm form);

    /**
     * 分页查询分类列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    Page<CategoryVO> getCategoryPage(Integer pageNum, Integer pageSize, String type);

    /**
     * 根据id查询分类
     * @param id
     * @param type
     * @return
     */
    CategorySimpleVO getCategoryById(Integer id, String type);

    /**
     * 查询分类列表
     * @param type
     * @return
     */
    List<CategoryVO> getCategory(String type);

    /**
     * 更新分类
     * @param id
     * @param form
     * @return
     */
    Boolean updateCategory(Integer id, CategoryForm form);

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    Boolean deleteCategory(Integer id);

}
