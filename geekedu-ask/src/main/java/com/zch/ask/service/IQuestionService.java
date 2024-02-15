package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.api.vo.ask.QuestionVO;
import com.zch.ask.domain.po.Question;

/**
 * @author Poison02
 * @date 2024/2/2
 */
public interface IQuestionService extends IService<Question> {

    QuestionAndCategoryVO getQuestionPage(Integer pageNum, Integer pageSize, String sort, String order,
                                          String keywords, Long userId, Integer categoryId, Integer status);
}
