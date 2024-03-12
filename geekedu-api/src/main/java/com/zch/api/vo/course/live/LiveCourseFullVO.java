package com.zch.api.vo.course.live;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import com.zch.common.mvc.result.PageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveCourseFullVO extends BaseVO {

    private PageResult.Data<LiveCourseVO> courses = new PageResult.Data<>();

    /**
     * 课程分类列表
     */
    private List<CategorySimpleVO> categories = new ArrayList<>(0);

    /**
     * 教师列表
     */
    private List<UserSimpleVO> teachers = new ArrayList<>(0);

    /**
     * 状态列表
     */
    private List<Map<String, Object>> statusList = new ArrayList<>(0);

}
