package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.course.CourseAndCategoryVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
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
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
                || ObjectUtils.isNull(sort) || ObjectUtils.isNull(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        // 查询课程数
        long count = count();
        if (count == 0) {
            vo.getCourses().setData(new ArrayList<>(0));
            vo.getCourses().setTotal(0);
            vo.setCategories(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Course::getTitle, keywords);
        }
        if (ObjectUtils.isNotNull(cid)) {
            wrapper.eq(Course::getCategoryId, cid);
        }
        if (ObjectUtils.isNotNull(id)) {
            wrapper.eq(Course::getId, id);
        }
        // 前端固定传入 id desc 后端还是简单做下兼容
        wrapper.orderBy(true, "desc".equals(order), Course::getId);
        Page<Course> page = page(new Page<Course>(pageNum, pageSize), wrapper);
        // 查询课程列表
        List<Course> list = page.getRecords();
        if (CollUtils.isEmpty(list)) {
            vo.getCourses().setData(new ArrayList<>(0));
            vo.getCourses().setTotal(0);
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
            CategorySimpleVO data = response.getData();
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
