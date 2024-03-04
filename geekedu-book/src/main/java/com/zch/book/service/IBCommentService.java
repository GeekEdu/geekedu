package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.book.AddCommentForm;
import com.zch.api.vo.book.comment.BCommentFullVO;
import com.zch.api.vo.book.comment.BCommentVO;
import com.zch.api.vo.book.comment.CommentVO;
import com.zch.book.domain.po.BComment;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/3
 */
public interface IBCommentService extends IService<BComment> {

    /**
     * 前台 图文 获取评论列表
     * @param relationId
     * @param pageNum
     * @param pageSize
     * @param commentId
     * @param cType
     * @return
     */
    Page<CommentVO> getCommentPage(Integer relationId, Integer pageNum, Integer pageSize, Integer commentId, String cType);

    /**
     * 根据id获取评论明细
     * @param commentId
     * @param cType
     * @return
     */
    CommentVO getCommentById(Integer commentId, String cType);

    /**
     * 发表评论
     * @param id
     * @param form
     * @param cType
     * @return
     */
    Integer addComment(Integer id, AddCommentForm form, String cType);

    /**
     * 电子书 获取评论列表
     * @param relationId
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    BCommentFullVO getFullComment(Integer relationId, Integer pageNum, Integer pageSize, String type);

    //===============================================================================================

    /**
     * 后台 获取图文评论列表 只需要将所有有关图文评论查出来即可，不需要划分等级评论
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    Page<BCommentVO> getBackendComments(Integer pageNum, Integer pageSize, String cType, List<String> createdTime);

    /**
     * 后台 根据id删除评论
     * @param id
     * @param cType
     * @return
     */
    Boolean deleteCommentById(Integer id, String cType);

}
