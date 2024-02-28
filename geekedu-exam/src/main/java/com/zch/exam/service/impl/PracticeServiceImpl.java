package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.ChapterForm;
import com.zch.api.dto.exam.DelChapterForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.ChapterVO;
import com.zch.api.vo.exam.PracticeVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.exam.domain.po.Practice;
import com.zch.exam.mapper.PracticeMapper;
import com.zch.exam.service.IChapterService;
import com.zch.exam.service.IPracticeService;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PracticeServiceImpl extends ServiceImpl<PracticeMapper, Practice> implements IPracticeService {

    private final ITagsService tagsService;

    private final IChapterService chapterService;

    @Override
    public Page<PracticeVO> getPracticePage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer categoryId) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
                || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        Page<PracticeVO> vo = new Page<>();
        long count = count();
        if (count == 0) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<Practice> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(Practice::getName, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(Practice::getCategoryId, categoryId);
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(order), Practice::getId);
        Page<Practice> page = page(new Page<Practice>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        List<Practice> records = page.getRecords();
        List<PracticeVO> list = new ArrayList<>(records.size());
        for (Practice item : records) {
            PracticeVO vo1 = new PracticeVO();
            BeanUtils.copyProperties(item, vo1);
            CTagsVO tag = tagsService.getSimpleTagById(item.getCategoryId(), "PAPERS");
            if (ObjectUtils.isNull(tag)) {
                vo1.setCategory(null);
            }
            vo1.setCategory(tag);
            list.add(vo1);
        }
        vo.setTotal(count);
        vo.setRecords(list);
        return vo;
    }

    @Override
    public PracticeVO getPracticeById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return new PracticeVO();
        }
        Practice Practice = getById(id);
        if (ObjectUtils.isNull(Practice)) {
            return new PracticeVO();
        }
        PracticeVO vo = new PracticeVO();
        BeanUtils.copyProperties(Practice, vo);
        CTagsVO tag = tagsService.getSimpleTagById(Practice.getCategoryId(), "PAPERS");
        if (ObjectUtils.isNull(tag)) {
            vo.setCategory(null);
        }
        vo.setCategory(tag);
        return vo;
    }

    @Override
    public List<CTagsVO> getTagList() {
        List<CTagsVO> list = tagsService.getSTagsList("PAPERS");
        if (ObjectUtils.isNull(list)) {
            return new ArrayList<>(0);
        }
        return list;
    }

    @Override
    public List<ChapterVO> getChapterList(Integer practiceId) {
        return chapterService.getChapterList(practiceId);
    }

    @Override
    public ChapterVO getChapterById(Integer id) {
        return chapterService.getChapterById(id);
    }

    @Override
    public Boolean deleteChapterBatch(DelChapterForm form) {
        return chapterService.deleteChapterBatch(form);
    }

    @Override
    public Boolean addChapter(ChapterForm form) {
        return chapterService.addChapter(form);
    }

    @Override
    public Boolean updateChapter(Integer id, ChapterForm form) {
        return chapterService.updateChapter(id, form);
    }
}
