package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.ask.domain.po.Question;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/2
 */
public interface IQuestionService extends IService<Question> {

    /**
     * 分页、条件查询问题列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param userId
     * @param categoryId
     * @param status
     * @param createdTimes
     * @return
     */
    QuestionAndCategoryVO getQuestionPage(Integer pageNum, Integer pageSize, String sort, String order,
                                          String keywords,
                                          String userId,
                                          Integer categoryId,
                                          Integer status,
                                          List<String> createdTimes);
}
