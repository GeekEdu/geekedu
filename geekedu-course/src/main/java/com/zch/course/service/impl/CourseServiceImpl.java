package com.zch.course.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.dto.course.LearnRecordForm;
import com.zch.api.dto.course.vod.CourseForm;
import com.zch.api.dto.course.vod.CourseSectionForm;
import com.zch.api.dto.user.CollectForm;
import com.zch.api.feignClient.comments.CommentsFeignClient;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.feignClient.resources.MediaFeignClient;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.api.vo.course.*;
import com.zch.api.vo.course.record.*;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.api.vo.resources.AttachVO;
import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.api.vo.system.search.SearchVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.meilisearch.result.SearchResult;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import com.zch.course.domain.dto.CourseMSDTO;
import com.zch.course.domain.dto.LiveCourseMSDTO;
import com.zch.course.domain.po.Course;
import com.zch.course.domain.po.CourseSection;
import com.zch.course.domain.po.LearnRecord;
import com.zch.course.mapper.CourseMapper;
import com.zch.course.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private final ILearnRecordService learnRecordService;

    private final TradeFeignClient tradeFeignClient;

    private final MeiliSearchService meiliSearchService;

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
    public Boolean addCourse(CourseForm form) {
        Course course = new Course();
        BeanUtils.copyProperties(form, course);
        save(course);
        //Course one = getOne(new LambdaQueryWrapper<Course>()
        //        .eq(Course::getTitle, form.getTitle())
        //        .eq(Course::getCategoryId, form.getCategoryId()));
        //CourseInfoEs esInfo = new CourseInfoEs();
        //esInfo.setCourseId(one.getId());
        //esInfo.setTitle(one.getTitle());
        //esInfo.setPrice(one.getPrice());
        //esInfo.setIntro(one.getIntro());
        //esInfo.setCoverLink(course.getCoverLink());
        //esInfo.setDescription(one.getDescription());
        //esInfo.setGroundingTime(one.getGroundingTime());
        //esInfo.setCreatedTime(one.getCreatedTime());
        //esInfo.setCategoryId(one.getCategoryId());
        //esInfo.setUpdatedTime(one.getUpdatedTime());
        //esInfo.setDocId(new IDWorkerUtil(1, 1, 1).nextId());
        //esCourseInfoService.insert(esInfo);
        return true;
    }

    @Override
    public Boolean updateCourse(Integer id, CourseForm form) {
        Course course = getById(id);
        course.setTitle(form.getTitle());
        course.setCategoryId(form.getCategoryId());
        course.setCoverLink(form.getCoverLink());
        course.setGroundingTime(form.getGroundingTime());
        course.setIsFree(form.getIsFree());
        course.setIsShow(form.getIsShow());
        course.setPrice(form.getPrice());
        course.setDescription(form.getDescription());
        course.setIntro(form.getIntro());
        return updateById(course);
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
    public Boolean addSection(CourseSectionForm form) {
        return sectionService.addSection(form);
    }

    @Override
    public Boolean updateSection(Integer sectionId, CourseSectionForm form) {
        return sectionService.updateSection(sectionId, form);
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
        // 不能查询后台设置为不显示的课程
        wrapper.eq(Course::getIsShow, 1);
        if (StringUtils.isNotBlank(scene)) {
            if ("free".equals(scene)) {
                // 免费
                wrapper.eq(Course::getPrice, 0);
            } else if ("sub".equals(scene)) {
                // 热门 根据销量降序排列
                wrapper.orderBy(true, false, Course::getSellNum);
            }
        }
        if (!Objects.equals(categoryId, 0)) {
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
        // Long userId = UserContext.getLoginId();
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
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
        // 是否购买课程
        if (!BigDecimal.ZERO.equals(course.getPrice())) {
            Response<Boolean> res1 = tradeFeignClient.queryOrderIsPay(userId, course.getId(), "REPLAY_COURSE");
            if (ObjectUtils.isNotNull(res1.getData())) {
                vo.setIsBuy(res1.getData());
            }
        }
        // 是否收藏课程
        Response<Boolean> res2 = userFeignClient.checkCollectStatus(course.getId(), "REPLAY_COURSE");
        if (ObjectUtils.isNotNull(res2.getData())) {
            vo.setIsCollect(res2.getData());
        }
        // 附件
        Response<List<AttachVO>> attach = mediaFeignClient.queryAttachList(id);
        if (ObjectUtils.isNotNull(attach) && ObjectUtils.isNotNull(attach.getData())) {
            vo.setAttach(attach.getData());
        }
        // 是否是会员
        Response<Boolean> res3 = userFeignClient.queryIsVip(userId);
        if (ObjectUtils.isNotNull(res3) && ObjectUtils.isNotNull(res3.getData())) {
            vo.setIsVip(res3.getData());
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
        // 视频观看进度
        if (vo.getIsBuy()) {
            Map<Integer, LearnRecordVO> videoWatchedProgress = new HashMap<>();
            sections.stream().forEach(item -> {
                LearnRecordVO record = learnRecordService.getLearnRecord(id, item.getId(), userId, "VOD");
                if (ObjectUtils.isNotNull(record)) {
                    videoWatchedProgress.put(item.getId(), record);
                }
            });
            vo.setVideoWatchedProgress(videoWatchedProgress);
        }
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
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
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
        // 是否购买课程
        Response<Boolean> res1 = tradeFeignClient.queryOrderIsPay(userId, course.getId(), "REPLAY_COURSE");
        if (ObjectUtils.isNotNull(res1.getData())) {
            vo.setIsWatch(res1.getData());
        }
        // 视频观看进度
        if (vo.getIsWatch()) {
            Map<Integer, LearnRecordVO> videoWatchedProgress = new HashMap<>();
            sections.stream().forEach(item -> {
                LearnRecordVO record = learnRecordService.getLearnRecord(section.getCourseId(), item.getId(), userId, "VOD");
                if (ObjectUtils.isNotNull(record)) {
                    videoWatchedProgress.put(item.getId(), record);
                }
            });
            vo.setVideoWatchedProgress(videoWatchedProgress);
        }
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

    @Override
    public Boolean courseRecord(Integer courseId, LearnRecordForm form) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        return learnRecordService.updateLearnRecord(courseId, form.getVideoId(), form.getDuration(), userId, "VOD");
    }

    @Override
    public Boolean courseStudy(Integer id, String type) {
        // 将课程学习记录进行初始化
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        // 查询课程信息，拿到对应的课程小节信息
        Course course = getById(id);
        if (ObjectUtils.isNotNull(course) && course.getSectionCount() != 0) {
            // 该课程存在视频
            // 查出课程的所有视频
            List<CourseSectionVO> sectionList = sectionService.getSectionList(id, 0);
            if (ObjectUtils.isNotNull(sectionList) && CollUtils.isNotEmpty(sectionList)) {
                sectionList.forEach(item -> {
                    LearnRecord learnRecord = new LearnRecord();
                    learnRecord.setCourseId(id);
                    learnRecord.setVideoId(item.getId());
                    learnRecord.setUserId(userId);
                    learnRecord.setType("VOD");
                    learnRecord.setDuration(0);
                    learnRecord.setTotal(Math.toIntExact(item.getDuration()));
                    learnRecordService.save(learnRecord);
                });
            }
        }
        return true;
    }

    @Override
    public List<LearnedCourseVO> getLearnedCourse(Integer pageNum, Integer pageSize) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        List<LearnRecord> records = learnRecordService.list(new LambdaQueryWrapper<LearnRecord>()
                .eq(LearnRecord::getUserId, userId)
                .eq(LearnRecord::getType, "VOD"));
        List<LearnedCourseVO> vo = new ArrayList<>();
        if (ObjectUtils.isNotNull(records) && CollUtils.isNotEmpty(records)) {
            List<Integer> courseIds = records.stream().map(LearnRecord::getCourseId).distinct().collect(Collectors.toList());
            courseIds.forEach(item -> {
                LearnedCourseVO vo1 = new LearnedCourseVO();
                Course course = getById(item);
                CourseSimpleVO cs = new CourseSimpleVO();
                BeanUtils.copyProperties(course, cs);
                vo1.setCourse(cs);
                // 根据课程id，将课程对应的小节查出来
                List<LearnRecord> records1 = learnRecordService.list(new LambdaQueryWrapper<LearnRecord>()
                        .eq(LearnRecord::getCourseId, item));
                if (ObjectUtils.isNotNull(records1) && CollUtils.isNotEmpty(records1)) {
                    // 使用一个集合，记录每个小节是否观看完
                    List<Boolean> isOk = new ArrayList<>();
                    records1.forEach(e -> {
                        // 判断观看时长是否有总时长的 2/3 如果有 则代表看完了
                        if (e.getDuration() >= Math.toIntExact(e.getTotal() * 2 / 3)) {
                            isOk.add(true);
                        } else {
                            isOk.add(false);
                        }
                    });
                    // 遍历 isOk 遇到true则将观看小节数 + 1
                    isOk.forEach(e -> {
                        if (e) {
                            vo1.setLearnedCount(vo1.getLearnedCount() + 1);
                        }
                    });
                    // 计算 progress 等于 learnedCount / sectionCount
                    vo1.setProgress((vo1.getLearnedCount() * 100 / course.getSectionCount()));
                    vo1.setIsOver(vo1.getProgress() >= 100);
                }
                vo.add(vo1);
            });
        }
        return vo;
    }

    @Override
    public List<LearnedDetailVO> getLearnDetail(Integer id) {
        List<LearnRecord> records = learnRecordService.list(new LambdaQueryWrapper<LearnRecord>()
                .eq(LearnRecord::getCourseId, id));
        if (ObjectUtils.isNull(records) || CollUtils.isEmpty(records)) {
            return new ArrayList<>(0);
        }
        List<LearnedDetailVO> vo = new ArrayList<>(records.size());
        records.forEach(item -> {
            // 查询每个小节的信息
            LearnedDetailVO vo1 = new LearnedDetailVO();
            CourseSection section = sectionService.getOne(new LambdaQueryWrapper<CourseSection>()
                    .eq(CourseSection::getId, item.getVideoId())
                    .eq(CourseSection::getCourseId, item.getCourseId())
                    .select(CourseSection::getTitle, CourseSection::getDuration));
            vo1.setTitle(section.getTitle());
            vo1.setId(item.getId());
            vo1.setTotal(Math.toIntExact(section.getDuration()));
            vo1.setDuration(item.getDuration());
            vo1.setVideoId(item.getVideoId());
            vo.add(vo1);
        });
        return vo;
    }

    @Override
    public Boolean courseCollect(Integer id) {
        CollectForm form = new CollectForm();
        form.setRelationId(id);
        form.setType("REPLAY_COURSE");
        return userFeignClient.hitCollectIcon(form).getData();
    }

    @Override
    public BigDecimal queryCoursePrice(Integer id) {
        Course course = getById(id);
        if (ObjectUtils.isNull(course)) {
            return BigDecimal.ZERO;
        }
        return course.getPrice();
    }

    @Override
    public SearchFullVO searchCourse(Integer offset, Integer limit, String type, String keyword) {
        // 查询录播课和直播课
        SearchResult<CourseMSDTO> courseResult = meiliSearchService.courseSearch(offset, limit, keyword);
        SearchResult<LiveCourseMSDTO> liveResult = meiliSearchService.liveCourseSearch(offset, limit, keyword);
        SearchFullVO vo = new SearchFullVO();
        List<SearchVO> vos = new ArrayList<>();
        List<SearchVO> vos1 = new ArrayList<>();
        List<SearchVO> vos2 = new ArrayList<>();
        if (StringUtils.isNotBlank(type)) {
            if (ObjectUtils.isNotNull(courseResult) && ObjectUtils.isNotNull(courseResult.getHits())
                    && CollUtils.isNotEmpty(courseResult.getHits())) {
                courseResult.getHits().forEach(item -> {
                    SearchVO vo1 = new SearchVO();
                    vo1.setId(item.getId());
                    vo1.setResourceId(item.getId());
                    vo1.setTitle(item.getTitle());
                    vo1.setResourceType("vod");
                    vo1.setDescription(item.getDescription());
                    vos1.add(vo1);
                });
            }
            if (ObjectUtils.isNotNull(liveResult) && ObjectUtils.isNotNull(liveResult.getHits())
                    && CollUtils.isNotEmpty(liveResult.getHits())) {
                liveResult.getHits().forEach(item -> {
                    SearchVO vo1 = new SearchVO();
                    vo1.setId(item.getId());
                    vo1.setResourceId(item.getId());
                    vo1.setTitle(item.getTitle());
                    vo1.setResourceType("vod");
                    vo1.setDescription(item.getIntro());
                    vos2.add(vo1);
                });
            }
            if ("all".equals(type)) {
                vos.addAll(vos1);
                vos.addAll(vos2);
                vo.setData(vos);
                vo.setTotal((long) vos.size());
                return vo;
            } else if ("vod".equals(type)) {
                vo.setData(vos1);
                vo.setTotal((long) vos1.size());
                return vo;
            } else if ("live".equals(type)) {
                vo.setData(vos2);
                vo.setTotal((long) vos2.size());
                return vo;
            }
        }
        return vo;
    }


}
