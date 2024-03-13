package com.zch.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.live.LiveChapterVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.course.domain.po.LiveChapter;
import com.zch.course.mapper.LiveChapterMapper;
import com.zch.course.service.ILiveChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveChapterServiceImpl extends ServiceImpl<LiveChapterMapper, LiveChapter> implements ILiveChapterService {

    private final LiveChapterMapper chapterMapper;

    @Override
    public List<LiveChapterVO> getChapterList(Integer courseId) {
        if (ObjectUtils.isNull(courseId)) {
            return new ArrayList<>(0);
        }
        List<LiveChapter> list = chapterMapper.selectList(new LambdaQueryWrapper<LiveChapter>()
                .eq(LiveChapter::getCourseId, courseId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<LiveChapterVO> vos = list.stream().map(item -> {
            LiveChapterVO vo = new LiveChapterVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public Boolean deleteChapterById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        LiveChapter chapter = chapterMapper.selectOne(new LambdaQueryWrapper<LiveChapter>()
                .eq(LiveChapter::getId, id));
        if (ObjectUtils.isNull(chapter)) {
            return false;
        }
        return removeById(id);
    }

    @Override
    public Boolean addChapter(ChapterForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        LiveChapter one = chapterMapper.selectOne(new LambdaQueryWrapper<LiveChapter>()
                .eq(LiveChapter::getName, form.getName()));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        LiveChapter chapter = new LiveChapter();
        chapter.setName(form.getName());
        chapter.setCourseId(form.getCourseId());
        chapter.setSort(form.getSort());
        chapter.setCreatedBy(userId);
        chapter.setUpdatedBy(userId);
        return save(chapter);
    }

    @Override
    public Boolean updateChapter(Integer id, ChapterForm form) {
        if (ObjectUtils.isNull(id) || ObjectUtils.isNull(form)) {
            return false;
        }
        LiveChapter one = chapterMapper.selectOne(new LambdaQueryWrapper<LiveChapter>()
                .eq(LiveChapter::getName, form.getName())
                .eq(LiveChapter::getId, id));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        LiveChapter chapter = new LiveChapter();
        chapter.setId(id);
        chapter.setName(form.getName());
        chapter.setSort(form.getSort());
        chapter.setUpdatedBy(userId);
        return updateById(chapter);
    }

    @Override
    public LiveChapterVO getChapterById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new LiveChapterVO();
        }
        LiveChapter chapter = chapterMapper.selectOne(new LambdaQueryWrapper<LiveChapter>()
                .eq(LiveChapter::getId, id));
        if (ObjectUtils.isNull(chapter)) {
            return new LiveChapterVO();
        }
        LiveChapterVO vo = new LiveChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        return vo;
    }
}
