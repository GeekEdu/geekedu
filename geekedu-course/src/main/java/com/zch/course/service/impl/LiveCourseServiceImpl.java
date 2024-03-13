package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.live.LiveCourseForm;
import com.zch.api.feignClient.comments.CommentsFeignClient;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.course.CourseCommentsVO;
import com.zch.api.vo.course.live.*;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.course.domain.po.LiveCourse;
import com.zch.course.mapper.LiveCourseMapper;
import com.zch.course.service.ILiveChapterService;
import com.zch.course.service.ILiveCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements ILiveCourseService {

    private final ILiveChapterService chapterService;

    private final UserFeignClient userFeignClient;

    private final LabelFeignClient labelFeignClient;

    private final CommentsFeignClient commentsFeignClient;

    @Override
    public LiveCourseFullVO getLiveCourseFullList(Integer pageNum, Integer pageSize, String sort, String order,
                                                  String keywords, Integer categoryId, Long teacherId, Integer status) {
        LiveCourseFullVO vo = new LiveCourseFullVO();
        // 查询课程分类列表
        vo.setCategories(labelFeignClient.getCategoryList("LIVE_COURSE").getData());
        // 查询教师列表
        vo.setTeachers(userFeignClient.getTeacherList().getData());
        // 构建课程状态列表
        vo.setStatusList(buildStatusList());
        long count = count();
        if (count == 0) {
            vo.getCourses().setTotal(0);
            vo.getCourses().setData(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<LiveCourse> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(LiveCourse::getTitle, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(LiveCourse::getCategoryId, categoryId);
        }
        if (ObjectUtils.isNotNull(teacherId)) {
            wrapper.eq(LiveCourse::getTeacherId, teacherId);
        }
        if (ObjectUtils.isNotNull(status) && ! Objects.equals(status, 0)) {
            wrapper.eq(LiveCourse::getStatus, status);
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(order), LiveCourse::getId);
        Page<LiveCourse> page = page(new Page<LiveCourse>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getCourses().setTotal(0);
            vo.getCourses().setData(new ArrayList<>(0));
            return vo;
        }
        vo.getCourses().setData(page.getRecords().stream().map(item -> {
            LiveCourseVO vo1 = new LiveCourseVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setCategory(labelFeignClient.getCategoryById(item.getCategoryId(), "LIVE_COURSE").getData());
            vo1.setTeacher(userFeignClient.getUserById(item.getTeacherId() + "").getData());
            vo1.setStatus(item.getStatus().getCode());
            vo1.setStatusText(item.getStatus().getValue());
            return vo1;
        }).collect(Collectors.toList()));
        vo.getCourses().setTotal(count);
        return vo;
    }

    @Override
    public Boolean updateLiveCourse(Integer courseId, LiveCourseForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        LiveCourse course = getById(courseId);
        course.setCategoryId(form.getCategoryId());
        course.setCover(form.getCover());
        course.setTitle(form.getTitle());
        course.setIsShow(form.getIsShow());
        course.setPrice(new BigDecimal(form.getPrice()));
        course.setIntro(form.getIntro());
        course.setRenderDesc(form.getRenderDesc());
        course.setVipCanView(form.getVipCanView());
        course.setTeacherId(form.getTeacherId());
        course.setGroundingTime(form.getGroundingTime());
        return updateById(course);
    }

    @Override
    public Boolean addLiveCourse(LiveCourseForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        LiveCourse course = new LiveCourse();
        course.setCategoryId(form.getCategoryId());
        course.setCover(form.getCover());
        course.setTitle(form.getTitle());
        course.setIsShow(form.getIsShow());
        course.setPrice(new BigDecimal(form.getPrice()));
        course.setIntro(form.getIntro());
        course.setRenderDesc(form.getRenderDesc());
        course.setVipCanView(form.getVipCanView());
        course.setTeacherId(form.getTeacherId());
        course.setGroundingTime(form.getGroundingTime());
        return save(course);
    }

    @Override
    public Boolean deleteLiveCourse(Integer courseId) {
        return removeById(courseId);
    }

    @Override
    public LiveCourseVO getLiveCourseDetail(Integer courseId) {
        LiveCourse course = getById(courseId);
        if (ObjectUtils.isNull(course)) {
            return new LiveCourseVO();
        }
        LiveCourseVO vo = new LiveCourseVO();
        BeanUtils.copyProperties(course, vo);
        vo.setCategory(labelFeignClient.getCategoryById(course.getCategoryId(), "LIVE_COURSE").getData());
        return vo;
    }

    @Override
    public LiveCategoryVO getCategoryList() {
        LiveCategoryVO vo = new LiveCategoryVO();
        vo.setCategories(labelFeignClient.getCategoryList("LIVE_COURSE").getData());
        vo.setTeachers(userFeignClient.getTeacherList().getData());
        return vo;
    }

    @Override
    public List<LiveChapterVO> getChapterList(Integer courseId) {
        return chapterService.getChapterList(courseId);
    }

    @Override
    public Boolean deleteChapterById(Integer id) {
        return chapterService.deleteChapterById(id);
    }

    @Override
    public Boolean addChapter(ChapterForm form) {
        return chapterService.addChapter(form);
    }

    @Override
    public Boolean updateChapter(Integer id, ChapterForm form) {
        return chapterService.updateChapter(id, form);
    }

    @Override
    public LiveChapterVO getChapterById(Integer id) {
        return chapterService.getChapterById(id);
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
        data.forEach(item -> {
            UserSimpleVO user = userFeignClient.getUserById(item.getUserId() + "").getData();
            item.setUser(user);
            item.setLiveCourse(getCourseSimpleById(item.getRelationId()));
        });
        vo.setData(response.getData());
        return vo;
    }

    @Override
    public LiveCourseSimpleVO getCourseSimpleById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new LiveCourseSimpleVO();
        }
        LiveCourse course = getById(id);
        LiveCourseSimpleVO vo = new LiveCourseSimpleVO();
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

    /**
     * 构建状态列表
     * @return
     */
    private List<Map<String, Object>> buildStatusList() {
        List<Map<String, Object>> res = new ArrayList<>(4);
        Map<String, Object> status = new HashMap<>(1);
        status.put("key", 0);
        status.put("name", "全部");
        Map<String, Object> status1 = new HashMap<>(1);
        status1.put("key", 1);
        status1.put("name", "未开课");
        Map<String, Object> status2 = new HashMap<>(1);
        status2.put("key", 2);
        status2.put("name", "已开课");
        Map<String, Object> status3 = new HashMap<>(1);
        status3.put("key", 3);
        status3.put("name", "已完结");
        res.add(status);
        res.add(status1);
        res.add(status2);
        res.add(status3);
        return res;
    }
}
