package com.zch.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.course.domain.po.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Poison02
 * @date 2024/1/25
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
