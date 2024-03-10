package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.CommentAnswerForm;
import com.zch.api.dto.ask.ReplyQuestionForm;
import com.zch.api.dto.user.ThumbForm;
import com.zch.api.vo.ask.AnswerAndCommentsVO;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.domain.po.Answer;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
public interface IAnswerService extends IService<Answer> {

    /**
     * 查找某个问题下的所有回答
     * @param id
     * @return
     */
    List<AnswersVO> getAnswersByQuestionId(Integer id);

    /**
     * 查找某个问题下所有回答和评论
     * @param id
     * @return
     */
    List<AnswerAndCommentsVO> getAnswerAndComments(Integer id);

    /**
     * 查找某个回答下的所有评论
     * @param id
     * @return
     */
    List<CommentsVO> getCommentsByAnswerId(Integer id);

    /**
     * 根据id删除回答
     * @param questionId
     * @param answerId
     * @return
     */
    Boolean deleteAnswerByAnswerId(Integer questionId, Integer answerId);

    /**
     * 删除回答下的评论
     * @param answerId 回答id
     * @param commentsId 评论id
     * @return
     */
    Boolean deleteCommentsById(Integer answerId, Integer commentsId, String type);

    /**
     * 设置为正确答案
     * @param answerId
     * @return
     */
    Boolean setAnswerCorrectByAnswerId(Integer answerId);

    /**
     * 查看是否正确答案
     * @param answerId
     * @return
     */
    Boolean isCorrectAnswer(Integer answerId);

    /**
     * 回答问题
     * @param id
     * @param form
     * @return
     */
    Boolean replyQuestion(Integer id, ReplyQuestionForm form);

    /**
     * 评论回答
     * @param id
     * @param form
     * @return
     */
    Boolean commentAnswer(Integer id, CommentAnswerForm form);

    /**
     * 分页查找评论
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<CommentsVO> getCommentsPage(Integer id, Integer pageNum, Integer pageSize);

    /**
     * 点赞评论
     * @param form
     * @return
     */
    Boolean thumbAnswer(ThumbForm form);

}
