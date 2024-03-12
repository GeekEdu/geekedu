package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.course.live.LiveChapterVO;
import com.zch.api.vo.course.live.LiveCourseFullVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.course.domain.po.LiveCourse;
import com.zch.course.mapper.LiveCourseMapper;
import com.zch.course.service.ILiveChapterService;
import com.zch.course.service.ILiveCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public LiveCourseFullVO getLiveCourseFullList(Integer pageNum, Integer pageSize, String sort, String order,
                                                  String keywords, Integer categoryId, Long teacherId, Integer status) {
        LiveCourseFullVO vo = new LiveCourseFullVO();
        // 查询课程分类列表
        vo.setCategories(labelFeignClient.getCategorySimpleList("LIVE_COURSE").getData());
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
            return vo1;
        }).collect(Collectors.toList()));
        vo.getCourses().setTotal(count);
        return vo;
    }

    @Override
    public List<LiveChapterVO> getChapterList(Integer courseId) {
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
    public LiveChapterVO getChapterById(Integer courseId, Integer id) {
        return chapterService.getChapterById(courseId, id);
    }

    private List<Map<String, Object>> buildStatusList() {
        List<Map<String, Object>> res = new ArrayList<>(4);
        Map<String, Object> status = new HashMap<>(1);
        status.put("key", 0);
        status.put("name", "全部");
        Map<String, Object> status1 = new HashMap<>(1);
        status.put("key", 1);
        status.put("name", "未开课");
        Map<String, Object> status2 = new HashMap<>(1);
        status.put("key", 2);
        status.put("name", "已开课");
        Map<String, Object> status3 = new HashMap<>(1);
        status.put("key", 3);
        status.put("name", "已完结");
        res.add(status);
        res.add(status1);
        res.add(status2);
        res.add(status3);
        return res;
    }
}
