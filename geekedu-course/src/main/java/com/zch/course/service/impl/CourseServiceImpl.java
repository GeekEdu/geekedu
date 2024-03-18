package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.feignClient.comments.CommentsFeignClient;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.resources.MediaFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.course.*;
import com.zch.api.vo.course.record.PlayUrlVO;
import com.zch.api.vo.course.record.RecordCourseVO;
import com.zch.api.vo.course.record.RecordSectionVO;
import com.zch.api.vo.course.record.TestVO;
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
import com.zch.course.service.ICourseSectionService;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final ICourseSectionService sectionService;

    private final MediaFeignClient mediaFeignClient;

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
        Response<List<CategorySimpleVO>> res = labelFeignClient.getCategorySimpleList("REPLAY_COURSE");
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
    public CourseVO getCourseById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new CourseVO();
        }
        Course course = getById(id);
        CourseVO vo = new CourseVO();
        if (ObjectUtils.isNull(course)) {
            return vo;
        }
        BeanUtils.copyProperties(course, vo);
        vo.setCategory(labelFeignClient.getCategoryById(course.getCategoryId(), "REPLAY_COURSE").getData());
        return vo;
    }

    @Override
    public Boolean deleteCourseById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        return removeById(id);
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

    @Override
    public Page<CourseSectionVO> getSectionList(Integer pageNum, Integer pageSize, String sort, String order, Integer courseId) {
        return sectionService.getSectionsPage(pageNum, pageSize, sort, order, courseId);
    }

    @Override
    public CourseSectionVO getSectionById(Integer id) {
        return sectionService.getSectionById(id);
    }

    @Override
    public Boolean deleteSectionBatch(DelSectionBatchForm form) {
        return sectionService.deleteSectionBatch(form);
    }

    @Override
    public List<CategorySimpleVO> getCategorySimpleList() {
        Response<List<CategorySimpleVO>> response = labelFeignClient.getCategorySimpleList("REPLAY_COURSE");
        if (ObjectUtils.isNull(response) || ObjectUtils.isNull(response.getData()) || CollUtils.isEmpty(response.getData())) {
            return new ArrayList<>(0);
        }
        return response.getData();
    }

    @Override
    public Page<CourseVO> getCourseCondition(Integer pageNum, Integer pageSize, String scene, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize) || ObjectUtils.isNull(categoryId)) {
            pageNum = 1;
            pageSize = 10;
            categoryId = 0; // 在前台表示全部分类，这里自定义按照课程id升序
        }
        Page<CourseVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(scene)) {
            if ("free".equals(scene)) {
                // 免费
                wrapper.eq(Course::getPrice, 0);
            } else if ("sub".equals(scene)) {
                // 热门 根据销量降序排列
                wrapper.orderBy(true, false, Course::getSellNum);
            }
        }
        if (! Objects.equals(categoryId, 0)) {
            wrapper.eq(Course::getCategoryId, categoryId);
        }
        Page<Course> page = page(new Page<Course>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        List<Course> records = page.getRecords();
        List<CourseVO> list = new ArrayList<>(records.size());
        for (Course item : records) {
            CourseVO vo1 = new CourseVO();
            BeanUtils.copyProperties(item, vo1);
            Response<CategorySimpleVO> res = labelFeignClient.getCategoryById(item.getCategoryId(), "REPLAY_COURSE");
            if (ObjectUtils.isNull(res) || ObjectUtils.isNull(res.getData())) {
                vo1.setCategory(null);
            }
            vo1.setCategory(res.getData());
            list.add(vo1);
        }
        vo.setRecords(list);
        vo.setTotal(count);
        return vo;
    }

    @Override
    public RecordCourseVO getDetailCourse(Integer id) {
        RecordCourseVO vo = new RecordCourseVO();
        if (ObjectUtils.isNull(id)) {
            return vo;
        }
        // 查询是否存在该课程
        Course course = courseMapper.selectById(id);
        if (ObjectUtils.isNull(course)) {
            return vo;
        }
        // 构建课程返回信息
        CourseVO cv = new CourseVO();
        BeanUtils.copyProperties(course, cv);
        vo.setCourse(cv);
        // 查询课程对应的章节
        List<CourseChapterVO> chapters = chapterService.getChapterList(course.getId());
        if (ObjectUtils.isNotNull(chapters) && CollUtils.isNotEmpty(chapters)) {
            vo.setChapters(chapters);
        }
        // 查询课程对应的视频小节
        // 1. 如果没有章节
        Map<Integer, List<CourseSectionVO>> videos = new HashMap<>(0);
        // 查全部视频
        List<CourseSectionVO> sections = sectionService.getSectionList(id, 0);
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
                List<CourseSectionVO> notChapters = sections.stream().filter((item) -> item.getChapterId() == 0).collect(Collectors.toList());
                if (ObjectUtils.isNotNull(notChapters)) {
                    videos.put(0, notChapters);
                }
                // 将使用章节的视频查出，放入map对应章节id的位置即可
                for (CourseChapterVO item : chapters) {
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
        return commentsFeignClient.getCommentsList(courseId, "REPLAY_COURSE", 1, 10000).getData();
    }

    @Override
    public Boolean addCourseComment(Integer id, AddCommentForm form) {
        return commentsFeignClient.addComment(id, "REPLAY_COURSE", form).getData();
    }

    @Override
    public CommentsFullVO getSectionComments(Integer sectionId) {
        return commentsFeignClient.getCommentsList(sectionId, "REPLAY_COURSE_HOUR", 1, 10000).getData();
    }

    @Override
    public RecordSectionVO getSectionDetail(Integer sectionId) {
        RecordSectionVO vo = new RecordSectionVO();
        if (ObjectUtils.isNull(sectionId)) {
            return vo;
        }
        // 查询是否存在该课时
        CourseSectionVO section = sectionService.getSectionById(sectionId);
        if (ObjectUtils.isNull(section)) {
            return vo;
        }
        vo.setVideo(section);
        // 查询课程信息
        Course course = courseMapper.selectById(section.getCourseId());
        if (ObjectUtils.isNull(course)) {
            return vo;
        }
        // 构建课程返回信息
        CourseVO cv = new CourseVO();
        BeanUtils.copyProperties(course, cv);
        vo.setCourse(cv);
        // 查询课程对应的章节
        List<CourseChapterVO> chapters = chapterService.getChapterList(course.getId());
        if (ObjectUtils.isNotNull(chapters) && CollUtils.isNotEmpty(chapters)) {
            vo.setChapters(chapters);
        }
        // 查询课程对应的视频小节
        // 1. 如果没有章节
        Map<Integer, List<CourseSectionVO>> videos = new HashMap<>(0);
        // 查全部视频
        List<CourseSectionVO> sections = sectionService.getSectionList(course.getId(), 0);
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
                List<CourseSectionVO> notChapters = sections.stream().filter((item) -> item.getChapterId() == 0).collect(Collectors.toList());
                if (ObjectUtils.isNotNull(notChapters)) {
                    videos.put(0, notChapters);
                }
                // 将使用章节的视频查出，放入map对应章节id的位置即可
                for (CourseChapterVO item : chapters) {
                    videos.put(item.getId(), sections.stream()
                            .filter(e -> Objects.equals(e.getChapterId(), item.getId()))
                            .collect(Collectors.toList()));
                }
            }
        }
        vo.setVideos(videos);
        return vo;
    }

    @Override
    public Boolean addSectionComment(Integer sectionId, AddCommentForm form) {
        return commentsFeignClient.addComment(sectionId, "REPLAY_COURSE_HOUR", form).getData();
    }

    @Override
    public PlayUrlVO getSectionPlayUrl(Integer sectionId, Integer isTry) {
        PlayUrlVO vo = new PlayUrlVO();
        // 查找课时对应的视频id
        CourseSectionVO section = sectionService.getSectionById(sectionId);
        if (Objects.isNull(section)) {
            return vo;
        }
        List<TestVO> url = new ArrayList<>();
        TestVO test = new TestVO();
        test.setUrl(mediaFeignClient.getVideoPlayUrl(section.getVideoId()).getData());
        test.setName("");
        url.add(test);
        vo.setUrl(url);
        return vo;
    }


}
