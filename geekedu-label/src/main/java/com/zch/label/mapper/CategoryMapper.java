package com.zch.label.mapper;

import com.zch.label.domain.po.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 查找所有分类
     * @return
     */
    List<Category> selectCategoryList();

    /**
     * 条件查询所有分类
     * @return
     */
    List<Category> selectCategoryByCondition();

    /**
     * 新增分类
     * @param category
     * @return
     */
    int insertCategory(Category category);

    /**
     * 删除分类
     * @param category
     * @return
     */
    int deleteCategory(Category category);

    /**
     * 更新分类
     * @param category
     * @return
     */
    int updateCategory(Category category);

}
