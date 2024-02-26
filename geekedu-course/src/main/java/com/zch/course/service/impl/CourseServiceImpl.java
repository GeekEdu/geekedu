package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.feignClient.comments.CommentsFeignClient;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.course.*;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.course.domain.po.Course;
import com.zch.course.mapper.CourseMapper;
import com.zch.course.service.ICourseChapterService;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final CommentsFeignClient commentsFeignClient;

    private final UserFeignClient userFeignClient;

    private final ICourseChapterService chapterService;

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
        wrapper.orderBy(true, "asc".equals(order), Course::getId);
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

    @Override
    public CourseCommentsVO getCommentsList(Integer pageNum, Integer pageSize, String cType, List<String> createdTime) {
        CourseCommentsVO vo = new CourseCommentsVO();
        PageResult<CommentsVO> response = commentsFeignClient.getCommentsPage(pageNum, pageSize, cType, createdTime);
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData())) {
            vo.setData(null);
        }
        List<CommentsVO> data = response.getData().getData();
        if (ObjectUtils.isNull(data) || CollUtils.isEmpty(data)) {
            vo.setUsers(new HashMap<>(0));
        }
        // 拿到所有评论的用户id
        List<Long> list = data.stream().map(CommentsVO::getUserId).distinct().collect(Collectors.toList());
        // 构造一个map类型
        Map<Long, UserSimpleVO> map = new HashMap<>(list.size());
        for (Long item : list) {
            Response<UserSimpleVO> res = userFeignClient.getUserById(item + "");
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                map.put(item, null);
            }
            map.put(item, res.getData());
        }
        vo.setUsers(map);
        vo.setData(response.getData());
        return vo;
    }

    @Override
    public CourseSimpleVO getCourseSimpleById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new CourseSimpleVO();
        }
        Course course = courseMapper.selectById(id);
        CourseSimpleVO vo = new CourseSimpleVO();
        BeanUtils.copyProperties(course, vo);
        return vo;
    }

    @Override
    public Boolean deleteBatchCourseComments(CommentsBatchDelForm form) {
        Response<Boolean> response = commentsFeignClient.deleteBatchComments(form);
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData())) {
            return false;
        }
        return response.getData();
    }

    @Override
    public List<CourseChapterVO> getChapterList(Integer courseId) {
        return chapterService.getChapterList(courseId);
    }

    @Override
    public Boolean deleteChapterById(Integer courseId, Integer id) {
        return chapterService.deleteChapterById(courseId, id);
    }

    @Override
    public Boolean addChapter(Integer courseId, ChapterForm form) {
        return chapterService.addChapter(courseId, form);
    }

    @Override
    public Boolean updateChapter(Integer courseId, Integer id, ChapterForm form) {
        return chapterService.updateChapter(courseId, id, form);
    }

    @Override
    public CourseChapterVO getChapterById(Integer courseId, Integer id) {
        return chapterService.getChapterById(courseId, id);
    }


}
