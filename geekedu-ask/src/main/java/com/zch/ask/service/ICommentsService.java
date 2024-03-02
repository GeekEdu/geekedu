package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.CommentAnswerForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.ask.CommentsForm;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.domain.po.Comments;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
public interface ICommentsService extends IService<Comments> {

    /**
     * 根据回答id查找所有评论
     * @param id
     * @return
     */
    List<CommentsVO> getCommentsByAnswerId(Integer id);

    /**
     * 根据id删除评论
     * @param id
     * @param type
     * @return
     */
    Boolean deleteComments(Integer id, String type);

    /**
     * 条件分页查评论列表
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    Page<CommentsVO> getCommentsPage(Integer pageNum, Integer pageSize, String cType, List<String> createdTime);

    /**
     * 批量删除评论
     * @param form
     * @return
     */
    Boolean deleteCommentsBatch(CommentsBatchDelForm form);

    void insertComments(CommentsForm form);

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
     * 前台 返回评论列表
     * @param id
     * @param cType
     * @param pageNum
     * @param pageSize
     * @return
     */
    CommentsFullVO getCommentsList(Integer id, String cType, Integer pageNum, Integer pageSize);

}
