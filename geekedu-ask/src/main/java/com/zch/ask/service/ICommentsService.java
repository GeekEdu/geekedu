package com.zch.ask.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

}