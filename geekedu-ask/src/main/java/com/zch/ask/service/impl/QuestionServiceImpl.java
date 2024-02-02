package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.ask.QuestionPageVO;
import com.zch.ask.domain.po.Question;
import com.zch.ask.mapper.QuestionMapper;
import com.zch.ask.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    private final QuestionMapper questionMapper;

    @Override
    public Page<QuestionPageVO> getQuestionByPage(Integer pageNum, Integer pageSize) {
        Page<Question> page = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Question>()
                        .eq(Question::getIsDelete, 0));
        return null;
    }
}
