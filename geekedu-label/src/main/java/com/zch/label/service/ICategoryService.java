package com.zch.label.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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

    Boolean addCategory(CategoryForm form);

    Page<CategoryVO> getCategoryPage(Integer pageNum, Integer pageSize, String type);

    CategorySimpleVO getCategoryById(Integer id, String type);

    List<CategoryVO> getCategory(String type);

}
