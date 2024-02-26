package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.CourseChapterVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.course.domain.po.CourseChapter;
import com.zch.course.mapper.CourseChapterMapper;
import com.zch.course.service.ICourseChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseChapterServiceImpl extends ServiceImpl<CourseChapterMapper, CourseChapter> implements ICourseChapterService {

    private final CourseChapterMapper chapterMapper;

    @Override
    public List<CourseChapterVO> getChapterList(Integer courseId) {
        if (ObjectUtils.isNull(courseId)) {
            return new ArrayList<>(0);
        }
        List<CourseChapter> list = chapterMapper.selectList(new LambdaQueryWrapper<CourseChapter>()
                .eq(CourseChapter::getCourseId, courseId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<CourseChapterVO> vos = list.stream().map(item -> {
            CourseChapterVO vo = new CourseChapterVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public Boolean deleteChapterById(Integer courseId, Integer id) {
        if (ObjectUtils.isNull(courseId) || ObjectUtils.isNull(id)) {
            return false;
        }
        CourseChapter chapter = chapterMapper.selectOne(new LambdaQueryWrapper<CourseChapter>()
                .eq(CourseChapter::getCourseId, courseId)
                .eq(CourseChapter::getId, id));
        if (ObjectUtils.isNull(chapter)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean addChapter(Integer courseId, ChapterForm form) {
        if (ObjectUtils.isNull(courseId) || ObjectUtils.isNull(form)) {
            return false;
        }
        CourseChapter one = chapterMapper.selectOne(new LambdaQueryWrapper<CourseChapter>()
                .eq(CourseChapter::getCourseId, courseId)
                .eq(CourseChapter::getName, form.getName()));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        CourseChapter chapter = new CourseChapter();
        chapter.setName(form.getName());
        chapter.setCourseId(courseId);
        chapter.setSort(form.getSort());
        chapter.setCreatedBy(userId);
        chapter.setUpdatedBy(userId);
        return save(chapter);
    }

    @Override
    public Boolean updateChapter(Integer courseId, Integer id, ChapterForm form) {
        if (ObjectUtils.isNull(courseId) || ObjectUtils.isNull(id) || ObjectUtils.isNull(form)) {
            return false;
        }
        CourseChapter one = chapterMapper.selectOne(new LambdaQueryWrapper<CourseChapter>()
                .eq(CourseChapter::getCourseId, courseId)
                .eq(CourseChapter::getName, form.getName())
                .eq(CourseChapter::getId, id));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        CourseChapter chapter = new CourseChapter();
        chapter.setId(id);
        chapter.setCourseId(courseId);
        chapter.setName(form.getName());
        chapter.setSort(form.getSort());
        chapter.setUpdatedBy(userId);
        return updateById(chapter);
    }

    @Override
    public CourseChapterVO getChapterById(Integer courseId, Integer id) {
        if (ObjectUtils.isNull(courseId) || ObjectUtils.isNull(id)) {
            return new CourseChapterVO();
        }
        CourseChapter chapter = chapterMapper.selectOne(new LambdaQueryWrapper<CourseChapter>()
                .eq(CourseChapter::getCourseId, courseId)
                .eq(CourseChapter::getId, id));
        if (ObjectUtils.isNull(chapter)) {
            return new CourseChapterVO();
        }
        CourseChapterVO vo = new CourseChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        return vo;
    }

}
