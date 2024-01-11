package com.zch.label.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.label.domain.po.Category;
import com.zch.label.domain.query.CategoryTagQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查找所有分类
     * @return
     */
    List<Category> selectCategoryList();

    /**
     * 条件查询所有分类
     * @return
     */
    List<Category> selectCategoryByCondition(CategoryTagQuery query);

    /**
     * 通过id查找分类
     * @param id
     * @return
     */
    Category selectCategoryById(Long id);

    /**
     * 根据分类名和分类类型查找分类
     * @param name
     * @param type
     * @return
     */
    Category selectCategoryByNameAndType(@Param("name") String name, @Param("type") Short type);

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
