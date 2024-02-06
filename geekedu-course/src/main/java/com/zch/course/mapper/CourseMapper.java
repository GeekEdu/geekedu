package com.zch.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.course.domain.po.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/25
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    Integer selectCount();

    List<Course> getCoursePageByCondition(@Param("pageNum") Integer pageNum,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("sort") String sort,
                                          @Param("order") String order,
                                          @Param("keywords") String keywords,
                                          @Param("cid") Integer cid,
                                          @Param("id") Integer id);

}
