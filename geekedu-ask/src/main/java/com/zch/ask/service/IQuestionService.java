package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.QuestionDeleteBatchForm;
import com.zch.api.dto.ask.QuestionForm;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.api.vo.ask.QuestionFullVO;
import com.zch.api.vo.label.CategorySimpleVO;
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
     * @param createdTime
     * @return
     */
    QuestionAndCategoryVO getQuestionPage(Integer pageNum, Integer pageSize, String sort, String order,
                                          String keywords,
                                          String userId,
                                          Integer categoryId,
                                          Integer status,
                                          List<String> createdTime);

    /**
     * 批量删除问题
     * @param form
     * @return
     */
    Boolean deleteQuestionBatchIds(QuestionDeleteBatchForm form);

    /**
     * 获取某个问题下的所有回答
     * @param id
     * @return
     */
    List<AnswersVO> getAnswersById(Integer id);

    /**
     * 根据id删除回答
     * @param questionId
     * @param answerId
     * @return
     */
    Boolean deleteAnswerById(Integer questionId, Integer answerId);

    /**
     * 设置正确答案
     * @param questionId
     * @param answerId
     * @return
     */
    Boolean setCorrectAnswer(Integer questionId, Integer answerId);

    ////////////////////////////////////////////////

    /**
     * 前台 返回问题列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    QuestionAndCategoryVO getV2Questions(Integer pageNum, Integer pageSize, String scene, Integer categoryId);

    /**
     * 返回问题-分类列表
     * @return
     */
    List<CategorySimpleVO> getTagList();

    /**
     * 返回问题明细
     * @param id
     * @return
     */
    QuestionFullVO getQuestionDetail(Integer id);

    /**
     * 新建问题
     * @param form
     * @return
     */
    Integer addQuestion(QuestionForm form);
}
