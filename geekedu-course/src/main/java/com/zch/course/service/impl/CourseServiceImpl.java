package com.zch.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.course.CourseAndCategoryVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import com.zch.course.domain.po.Course;
import com.zch.course.mapper.CourseMapper;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    private final CourseMapper courseMapper;

    private final LabelFeignClient labelFeignClient;

    @Override
    public CourseAndCategoryVO getCoursePage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer cid, Integer id) {
        CourseAndCategoryVO vo = new CourseAndCategoryVO();
        if (ObjectUtils.isNull(sort) || ObjectUtils.isNull(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        Integer page = (pageNum - 1) * pageSize;
        // 查询课程数
        Integer total = courseMapper.selectCount();
        if (total == 0) {
            vo.getCourses().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        // 查询课程列表
        List<Course> list = courseMapper.getCoursePageByCondition(page, pageSize, sort, order, keywords, cid, id);
        if (CollUtils.isEmpty(list)) {
            vo.getCourses().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        List<CourseVO> courses = new ArrayList<>(list.size());
        for (Course course : list) {
            Response<CategorySimpleVO> response = labelFeignClient.getCategoryById(course.getCategoryId(), "REPLAY_COURSE");
            if (response == null || response.getData() == null) {
                vo.getCourses().setData(new ArrayList<>(0));
                vo.setCategories(new ArrayList<>(0));
                return vo;
            }
            CategorySimpleVO data = (CategorySimpleVO) response.getData();
            CourseVO vo1 = new CourseVO();
            BeanUtils.copyProperties(course, vo1);
            vo1.setCategory(data);
            courses.add(vo1);
        }
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategoryList("REPLAY_COURSE");
        if (res == null || res.getData() == null) {
            vo.getCourses().setData(new ArrayList<>(0));
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        vo.setCategories(res.getData());
        vo.getCourses().setData(courses);
        vo.getCourses().setTotal(courses.size());
        return vo;
    }


}
