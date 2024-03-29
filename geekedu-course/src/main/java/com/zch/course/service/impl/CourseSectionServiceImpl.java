package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.dto.course.vod.CourseSectionForm;
import com.zch.api.vo.course.CourseChapterVO;
import com.zch.api.vo.course.CourseSectionVO;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.course.domain.po.Course;
import com.zch.course.domain.po.CourseChapter;
import com.zch.course.domain.po.CourseSection;
import com.zch.course.mapper.CourseChapterMapper;
import com.zch.course.mapper.CourseMapper;
import com.zch.course.mapper.CourseSectionMapper;
import com.zch.course.service.ICourseSectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseSectionServiceImpl extends ServiceImpl<CourseSectionMapper, CourseSection> implements ICourseSectionService {

    private final CourseMapper courseMapper;

    private final CourseChapterMapper chapterMapper;

    private final CourseSectionMapper sectionMapper;

    @Override
    public Page<CourseSectionVO> getSectionsPage(Integer pageNum, Integer pageSize, String sort, String order, Integer courseId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "groundingTime";
            order = "asc";
        }
        Page<CourseSectionVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<CourseSection> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotNull(courseId)) {
            wrapper.eq(CourseSection::getCourseId, courseId);
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(order), CourseSection::getGroundingTime);
        Page<CourseSection> page = page(new Page<CourseSection>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        List<CourseSection> records = page.getRecords();
        List<CourseSectionVO> list = new ArrayList<>(records.size());
        for (CourseSection item : records) {
            CourseSectionVO vo1 = new CourseSectionVO();
            BeanUtils.copyProperties(item, vo1);
            Course course = courseMapper.selectById(item.getCourseId());
            if (ObjectUtils.isNull(course)) {
                vo1.setCourse(null);
            }
            CourseSimpleVO csv = new CourseSimpleVO();
            BeanUtils.copyProperties(course, csv);
            vo1.setCourse(csv);
            CourseChapterVO ccv = new CourseChapterVO();
            CourseChapter chapter = chapterMapper.selectById(item.getChapterId());
            if (ObjectUtils.isNull(chapter)) {
                vo1.setChapter(null);
            }
            BeanUtils.copyProperties(chapter, ccv);
            vo1.setChapter(ccv);
            list.add(vo1);
        }
        vo.setTotal(count);
        vo.setRecords(list);
        return vo;
    }

    @Override
    public CourseSectionVO getSectionById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new CourseSectionVO();
        }
        CourseSection section = sectionMapper.selectById(id);
        CourseSectionVO vo = new CourseSectionVO();
        BeanUtils.copyProperties(section, vo);
        CourseChapter chapter = chapterMapper.selectById(section.getChapterId());
        CourseChapterVO ccv = new CourseChapterVO();
        if (ObjectUtils.isNull(chapter)) {
            vo.setChapter(null);
        }
        BeanUtils.copyProperties(chapter, ccv);
        vo.setChapter(ccv);
        Course course = courseMapper.selectById(section.getCourseId());
        CourseSimpleVO csv = new CourseSimpleVO();
        if (ObjectUtils.isNull(course)) {
            vo.setCourse(null);
        }
        BeanUtils.copyProperties(course, csv);
        vo.setCourse(csv);
        return vo;
    }

    @Transactional
    @Override
    public Boolean deleteSectionBatch(DelSectionBatchForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getIds()) || CollUtils.isEmpty(form.getIds())) {
            return false;
        }
        return removeBatchByIds(form.getIds());
    }

    @Override
    public Boolean addSection(CourseSectionForm form) {
        CourseSection section = new CourseSection();
        section.setCourseId(form.getCourseId());
        section.setChapterId(form.getChapterId());
        section.setTitle(form.getTitle());
        section.setDuration(form.getDuration());
        section.setGroundingTime(form.getGroundingTime());
        section.setFreeSeconds(form.getFreeSeconds());
        section.setBanDrag(form.getBanDrag());
        section.setIsShow(form.getIsShow());
        section.setTencentId(form.getTencentVideoId());
        section.setVideoId(form.getVideoId());
        return save(section);
    }

    @Override
    public Boolean updateSection(Integer sectionId, CourseSectionForm form) {
        CourseSection section = getById(sectionId);
        if (ObjectUtils.isNull(section)) {
            return false;
        }
        section.setCourseId(form.getCourseId());
        section.setChapterId(form.getChapterId());
        section.setTitle(form.getTitle());
        section.setDuration(form.getDuration());
        section.setGroundingTime(form.getGroundingTime());
        section.setFreeSeconds(form.getFreeSeconds());
        section.setBanDrag(form.getBanDrag());
        section.setIsShow(form.getIsShow());
        section.setTencentId(form.getTencentVideoId());
        section.setVideoId(form.getVideoId());
        return updateById(section);
    }

    @Override
    public List<CourseSectionVO> getSectionList(Integer courseId, Integer chapterId) {
        if (ObjectUtils.isNull(courseId)) {
            return new ArrayList<>(0);
        }
        LambdaQueryWrapper<CourseSection> wrapper = new LambdaQueryWrapper<>();
        if (!Objects.equals(chapterId, 0)) {
            // 不为0则查 对应课程及章节
            wrapper.eq(CourseSection::getCourseId, courseId)
                    .eq(CourseSection::getChapterId, chapterId);
        } else {
            // 不为0则查 对应课程
            wrapper.eq(CourseSection::getCourseId, courseId);
        }
        List<CourseSection> sections = sectionMapper.selectList(wrapper);
        if (ObjectUtils.isNull(sections) || CollUtils.isEmpty(sections)) {
            return new ArrayList<>(0);
        }
        return sections.stream().map(item -> {
            CourseSectionVO vo = new CourseSectionVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
