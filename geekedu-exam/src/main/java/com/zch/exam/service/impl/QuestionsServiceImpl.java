package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.exam.DeleteBatchQuestions;
import com.zch.api.dto.exam.ImportXlsxAddForm;
import com.zch.api.dto.exam.QuestionForm;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.*;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.exam.domain.po.Questions;
import com.zch.exam.mapper.QuestionsMapper;
import com.zch.exam.service.ILevelsService;
import com.zch.exam.service.IQuestionsService;
import com.zch.exam.service.ITagsService;
import com.zch.exam.service.ITypesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Boolean insertQuestionByImport(ImportXlsxAddForm form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getData()) || CollUtils.isEmpty(form.getData())) {
            return false;
        }
        List<List<String>> list = form.getData();
        boolean isOk = true;
        List<QuestionForm> data = new ArrayList<>(list.size());
        for (List<String> item : list) {
            QuestionForm req = giveValueBySize(item);
            data.add(req);
        }
        for (QuestionForm item : data) {
            isOk = addQuestion(item);
        }
        return isOk;
    }

    @Transactional
    @Override
    public Boolean deleteBatchQuestions(DeleteBatchQuestions form) {
        if (ObjectUtils.isNull(form) || ObjectUtils.isNull(form.getIds()) || CollUtils.isEmpty(form.getIds())) {
            return false;
        }
        return removeBatchByIds(form.getIds());
    }

    @Override
    public QuestionsFullVO getQuestionsTypeList() {
        QuestionsFullVO vo = new QuestionsFullVO();
        List<TagsVO> tagsList = tagsService.getTagsList("QUESTIONS");
        List<TypesVO> typesList = typesService.getTypesList();
        List<LevelsVO> levelsList = levelsService.getLevelsList();
        vo.setTypes(typesList);
        vo.setCategories(tagsList);
        vo.setLevels(levelsList);
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

    /**
     * 通用新增试题方法
     *
     * @param form
     * @return
     */
    private Boolean addQuestion(QuestionForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        // 判断是否有该分类 没有该分类，不继续创建，直接返回false
        TagsVO tag = tagsService.getTagByName(form.getCategory(), "QUESTIONS");
        if (ObjectUtils.isNull(tag)) {
            return false;
        }
        // 判断是否有该类型题
        TypesVO type = typesService.getTypeByName(form.getType());
        if (ObjectUtils.isNull(type)) {
            return false;
        }
        // 判断是否有该等级
        LevelsVO level = levelsService.getLevelsByName(form.getLevel());
        if (ObjectUtils.isNull(level)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        Questions questions = new Questions();
        BeanUtils.copyProperties(form, questions);
        questions.setCategoryId(tag.getId());
        questions.setLevels(level.getId());
        questions.setTypes(type.getId());
        questions.setScore(new BigDecimal(form.getScores()));
        questions.setCreatedBy(userId);
        questions.setUpdatedBy(userId);
        return save(questions);
    }

    /**
     *
     * @param data
     * @return
     */
    private QuestionForm giveValueBySize(List<String> data) {
        QuestionForm vo = new QuestionForm();
        int size = data.size();
        vo.setCategory(data.get(0) == null ? "" : data.get(0));
        vo.setType(data.get(1) == null ? "" : data.get(1));
        vo.setLevel(data.get(2) == null ? "" : data.get(2));
        vo.setContent(data.get(3) == null ? "" : data.get(3));
        vo.setAnswer(data.get(4) == null ? "" : data.get(4));
        vo.setAnalysis(data.get(5) == null ? "" : data.get(5));
        vo.setScores(data.get(6) == null ? "" : data.get(6));
        if (size == 8) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
        } else if (size == 9) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
        } else if (size == 10) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
        } else if (size == 11) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
        } else if (size == 12) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
        } else if (size == 13) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
            vo.setOption6(data.get(12) == null ? "" : data.get(12));
        } else if (size == 14) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
            vo.setOption6(data.get(12) == null ? "" : data.get(12));
            vo.setOption7(data.get(13) == null ? "" : data.get(13));
        } else if (size == 15) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
            vo.setOption6(data.get(12) == null ? "" : data.get(12));
            vo.setOption7(data.get(13) == null ? "" : data.get(13));
            vo.setOption8(data.get(14) == null ? "" : data.get(14));
        } else if (size == 16) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
            vo.setOption6(data.get(12) == null ? "" : data.get(12));
            vo.setOption7(data.get(13) == null ? "" : data.get(13));
            vo.setOption8(data.get(14) == null ? "" : data.get(14));
            vo.setOption9(data.get(15) == null ? "" : data.get(15));
        } else if (size == 17) {
            vo.setOption1(data.get(7) == null ? "" : data.get(7));
            vo.setOption2(data.get(8) == null ? "" : data.get(8));
            vo.setOption3(data.get(9) == null ? "" : data.get(9));
            vo.setOption4(data.get(10) == null ? "" : data.get(10));
            vo.setOption5(data.get(11) == null ? "" : data.get(11));
            vo.setOption6(data.get(12) == null ? "" : data.get(12));
            vo.setOption7(data.get(13) == null ? "" : data.get(13));
            vo.setOption8(data.get(14) == null ? "" : data.get(14));
            vo.setOption9(data.get(15) == null ? "" : data.get(15));
            vo.setOption10(data.get(16) == null ? "" : data.get(16));
        }
        return vo;
    }
}
