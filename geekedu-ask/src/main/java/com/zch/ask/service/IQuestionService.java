package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.ask.QuestionPageVO;
import com.zch.ask.domain.po.Question;

/**
 * @author Poison02
 * @date 2024/2/2
 */
public interface IQuestionService extends IService<Question> {

    Page<QuestionPageVO> getQuestionByPage(Integer pageNum, Integer pageSize);

}
