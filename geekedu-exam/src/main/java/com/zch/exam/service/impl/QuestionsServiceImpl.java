package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.exam.QuestionsVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.exam.domain.po.Questions;
import com.zch.exam.mapper.QuestionsMapper;
import com.zch.exam.service.IQuestionsService;
import com.zch.exam.service.ITagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
