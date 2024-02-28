package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.ChapterForm;
import com.zch.api.dto.exam.DelChapterForm;
import com.zch.api.vo.exam.ChapterVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.exam.domain.po.Chapter;
import com.zch.exam.mapper.ChapterMapper;
import com.zch.exam.service.IChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements IChapterService {

    private final ChapterMapper chapterMapper;

    @Override
    public List<ChapterVO> getChapterList(Integer practiceId) {
        if (ObjectUtils.isNull(practiceId)) {
            return new ArrayList<>(0);
        }
        List<Chapter> chapters = chapterMapper.selectList(new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getPracticeId, practiceId));
        if (ObjectUtils.isNull(chapters) || CollUtils.isEmpty(chapters)) {
            return new ArrayList<>(0);
        }
        List<ChapterVO> vos = chapters.stream().map(item -> {
            ChapterVO vo = new ChapterVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public ChapterVO getChapterById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        Chapter chapter = chapterMapper.selectById(id);
        ChapterVO vo = new ChapterVO();
        BeanUtils.copyProperties(chapter, vo);
        return vo;
    }

    @Transactional
    @Override
    public Boolean deleteChapterBatch(DelChapterForm form) {
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form.getIds())) {
            return false;
        }
        List<Integer> ids = form.getIds();
        return removeBatchByIds(ids);
    }

    @Override
    public Boolean addChapter(ChapterForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        Chapter chapter = new Chapter();
        Chapter one = chapterMapper.selectOne(new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getName, form.getName())
                .eq(Chapter::getSort, form.getSort()));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        chapter.setName(form.getName());
        chapter.setPracticeId(form.getPracticeId());
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
        Long userId = UserContext.getLoginId();
        Chapter byId = getById(id);
        if (ObjectUtils.isNull(byId)) {
            return false;
        }
        Chapter one = chapterMapper.selectOne(new LambdaQueryWrapper<Chapter>()
                .eq(Chapter::getName, form.getName())
                .eq(Chapter::getSort, form.getSort()));
        if (ObjectUtils.isNotNull(one)) {
            return false;
        }
        Chapter chapter = new Chapter();
        chapter.setId(byId.getId());
        chapter.setName(form.getName());
        chapter.setSort(form.getSort());
        chapter.setUpdatedBy(userId);
        return updateById(chapter);
    }
}
