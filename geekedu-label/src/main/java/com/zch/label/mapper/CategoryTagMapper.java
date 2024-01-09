package com.zch.label.mapper;

import com.zch.label.domain.po.CategoryTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryTagMapper {

    /**
     * 根据分类id查找该分类下的所有标签
     * @param id
     * @return
     */
    List<CategoryTag> selectTagByCategoryId(Long id);

    /**
     * 根据分类id和标签id查找数据
     * @param categoryId
     * @param tagId
     * @return
     */
    CategoryTag selectCategoryTagByCategoryIdAndTagId(@Param("categoryId") Long categoryId, @Param("tagId") Long tagId);

    /**
     * 更新分类信息
     * @param categoryTag
     * @return
     */
    int updateCategory(CategoryTag categoryTag);

    /**
     * 删除分类信息
     * @param categoryTag
     * @return
     */
    int deleteCategory(CategoryTag categoryTag);

    /**
     * 新增 CategoryTag
     * @param categoryTag
     * @return
     */
    int insertCategoryTag(CategoryTag categoryTag);

}
