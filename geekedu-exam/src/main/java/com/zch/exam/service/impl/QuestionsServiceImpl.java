package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.*;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.exam.domain.po.Questions;
import com.zch.exam.mapper.QuestionsMapper;
import com.zch.exam.service.ILevelsService;
import com.zch.exam.service.IQuestionsService;
import com.zch.exam.service.ITagsService;
import com.zch.exam.service.ITypesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements IQuestionsService {

    private final QuestionsMapper questionsMapper;

    private final ITagsService tagsService;

    private final ITypesService typesService;

    private final ILevelsService levelsService;

    @Override
    public QuestionsVO getQuestionsById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        Questions questions = questionsMapper.selectOne(new LambdaQueryWrapper<Questions>()
                .eq(Questions::getId, id));
        if (ObjectUtils.isNull(questions)) {
            return null;
        }
        QuestionsVO vo = new QuestionsVO();
        BeanUtils.copyProperties(questions, vo);
        TagsVO category = tagsService.getTagByCondition(questions.getCategoryId(), "QUESTIONS");
        if (ObjectUtils.isNull(category)) {
            vo.setCategory(null);
        }
        vo.setCategory(category);
        return vo;
    }

    @Override
    public QuestionsFullVO getQuestionPage(Integer pageNum, Integer pageSize, Integer categoryId, Integer type, Integer level) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        QuestionsFullVO vo = new QuestionsFullVO();
        LambdaQueryWrapper<Questions> wrapper = new LambdaQueryWrapper<>();
        long count = count();
        if (count == 0) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(Questions::getCategoryId, categoryId);
        }
        if (ObjectUtils.isNotNull(type)) {
            wrapper.eq(Questions::getTypes, type);
        }
        if (ObjectUtils.isNotNull(level)) {
            wrapper.eq(Questions::getLevels, level);
        }
        Page<Questions> page = page(new Page<Questions>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<Questions> records = page.getRecords();
        List<QuestionsVO> qLists = new ArrayList<>(records.size());
        for (Questions item : records) {
            QuestionsVO vo1 = new QuestionsVO();
            BeanUtils.copyProperties(item, vo1);
            TagsVO category = tagsService.getTagByCondition(item.getCategoryId(), "QUESTIONS");
            vo1.setCategory(category);
            qLists.add(vo1);
        }
        List<TagsVO> tagsList = tagsService.getTagsList("QUESTIONS");
        List<TypesVO> typesList = typesService.getTypesList();
        List<LevelsVO> levelsList = levelsService.getLevelsList();
        vo.setCategories(tagsList);
        vo.setLevels(levelsList);
        vo.setTypes(typesList);
        vo.getData().setTotal(count);
        vo.getData().setData(qLists);
        return vo;
    }

    @Override
    public Page<TagsVO> getCategoryList(Integer pageNum, Integer pageSize, String type) {
        Page<TagsVO> vo = new Page<>();
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize) || StringUtils.isBlank(type)) {
            vo.setRecords(new ArrayList<>(0));
            vo.setTotal(0);
            return vo;
        }
        return tagsService.getCategoryList(pageNum, pageSize, type);
    }

    @Override
    public Boolean deleteCategoryById(Integer id, String type) {
        if (ObjectUtils.isNull(id) || StringUtils.isBlank(type)) {
            return false;
        }
        return tagsService.deleteTag(id, type);
    }

    @Override
    public Boolean updateCategory(Integer id, TagForm form) {
        if (ObjectUtils.isNull(id) || ObjectUtils.isNull(form)) {
            return false;
        }
        return tagsService.updateTag(id, form);
    }

    @Override
    public Boolean addCategory(TagForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        return tagsService.addTag(form);
    }

    @Override
    public TagsVO getCategoryById(Integer id, String type) {
        if (ObjectUtils.isNull(id) || StringUtils.isBlank(type)) {
            return new TagsVO();
        }
        return tagsService.getTagByCondition(id, type);
    }
}
