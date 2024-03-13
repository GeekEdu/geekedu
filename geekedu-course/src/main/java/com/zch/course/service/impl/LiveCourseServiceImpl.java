package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.live.LiveCourseForm;
import com.zch.api.dto.course.live.LiveVideoForm;
import com.zch.api.feignClient.comments.CommentsFeignClient;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsFullVO;
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
import com.zch.course.service.ILiveVideoService;
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

    private final ILiveVideoService videoService;

    private final UserFeignClient userFeignClient;

    private final LabelFeignClient labelFeignClient;

    private final CommentsFeignClient commentsFeignClient;

    private final LiveCourseMapper courseMapper;

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

    @Override
    public Page<LiveVideoVO> getVideoList(Integer pageNum, Integer pageSize, Integer courseId) {
        Page<LiveVideoVO> page = videoService.getVideoList(pageNum, pageSize, courseId);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            return page;
        }
        page.getRecords().forEach(vo -> {
            vo.setCourse(getCourseSimpleById(vo.getCourseId()));
        });
        return page;
    }

    @Override
    public LiveVideoVO getVideoDetail(Integer id) {
        return videoService.getVideoDetail(id);
    }

    @Override
    public Boolean updateVideo(Integer id, LiveVideoForm form) {
        return videoService.updateVideo(id, form);
    }

    @Override
    public Boolean deleteVideo(Integer id) {
        return videoService.deleteVideo(id);
    }

    @Override
    public Boolean addVideo(LiveVideoForm form) {
        return videoService.addVideo(form);
    }

    @Override
    public LiveFrontVO getV2List(Integer pageNum, Integer pageSize, Integer categoryId) {
        LambdaQueryWrapper<LiveCourse> wrapper = new LambdaQueryWrapper<>();
        if (! Objects.equals(categoryId, 0)) {
            wrapper.eq(LiveCourse::getCategoryId, categoryId);
        }
        LiveFrontVO vo = new LiveFrontVO();
        // 查询分类列表
        vo.setCategories(labelFeignClient.getCategorySimpleList("LIVE_COURSE").getData());
        Page<LiveCourse> page = page(new Page<>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        vo.getData().setData(page.getRecords().stream().map(item -> {
            LiveCourseVO vo1 = new LiveCourseVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setCategory(labelFeignClient.getCategoryById(item.getCategoryId(), "LIVE_COURSE").getData());
            vo1.setTeacher(userFeignClient.getUserById(item.getTeacherId() + "").getData());
            return vo1;
        }).collect(Collectors.toList()));
        vo.getData().setTotal(page.getTotal());
        return vo;
    }

    @Override
    public LiveDetailVO getLiveDetail(Integer id) {
        LiveDetailVO vo = new LiveDetailVO();
        if (ObjectUtils.isNull(id)) {
            return vo;
        }
        // 查询是否存在该课程
        LiveCourse course = courseMapper.selectById(id);
        if (ObjectUtils.isNull(course)) {
            return vo;
        }
        // 构建课程返回信息
        LiveCourseVO cv = new LiveCourseVO();
        BeanUtils.copyProperties(course, cv);
        cv.setTeacher(userFeignClient.getUserById(course.getTeacherId() + "").getData());
        vo.setCourse(cv);
        // 查询课程对应的章节
        List<LiveChapterVO> chapters = chapterService.getChapterList(course.getId());
        if (ObjectUtils.isNotNull(chapters) && CollUtils.isNotEmpty(chapters)) {
            vo.setChapters(chapters);
        }
        // 查询课程对应的视频小节
        // 1. 如果没有章节
        Map<Integer, List<LiveVideoVO>> videos = new HashMap<>(0);
        // 查全部视频
        List<LiveVideoVO> sections = videoService.getVideoList(id);
        if (ObjectUtils.isNull(chapters) || CollUtils.isEmpty(chapters)) {
            // 没有视频
            if (ObjectUtils.isNull(sections) || CollUtils.isEmpty(sections)) {
                vo.setVideos(new HashMap<>(0));
                return vo;
            } else {
                // 存在视频 但是没有章节 直接放在map的第0个
                videos.put(0, sections);
            }
        } else {
            // 2. 有章节
            // 没有视频
            if (ObjectUtils.isNull(sections) || CollUtils.isEmpty(sections)) {
                vo.setVideos(new HashMap<>(0));
                return vo;
            } else {
                // 存在视频 且有章节 但是要考虑：1.所有视频都用了章节；2.所有视频都没用章节；3.部分视频用章节，部分没有
                // 将没有用章节的视频都查出来 放入 map 的 0 位置 没有用章节，只需要在全部视频中过滤出章节id为0的
                List<LiveVideoVO> notChapters = sections.stream().filter((item) -> item.getChapterId() == 0).collect(Collectors.toList());
                if (ObjectUtils.isNotNull(notChapters)) {
                    videos.put(0, notChapters);
                }
                // 将使用章节的视频查出，放入map对应章节id的位置即可
                for (LiveChapterVO item : chapters) {
                    videos.put(item.getId(), sections.stream()
                            .filter(e -> Objects.equals(e.getChapterId(), item.getId()))
                            .collect(Collectors.toList()));
                }
            }
        }
        vo.setVideos(videos);
        // TODO 是否购买课程
        // TODO 是否收藏课程
        // TODO 附件
        // TODO 视频观看进度
        return vo;
    }

    @Override
    public CommentsFullVO getCourseComments(Integer courseId) {
        return commentsFeignClient.getCommentsList(courseId, "LIVE_COURSE", 1, 10000).getData();
    }

    @Override
    public Boolean addCourseComment(Integer id, AddCommentForm form) {
        return commentsFeignClient.addComment(id, "LIVE_COURSE", form).getData();
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
