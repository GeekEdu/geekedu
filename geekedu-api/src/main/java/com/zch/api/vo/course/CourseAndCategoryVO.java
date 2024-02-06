package com.zch.api.vo.course;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAndCategoryVO extends BaseVO {

    /**
     * 分页课程列表
     */
    PageResult.Data<CourseVO> courses = new PageResult.Data<>();

    /**
     * 课程类型的所有分类
     */
    List<CategorySimpleVO> categories;

}
