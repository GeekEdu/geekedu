package com.zch.ask.controller;

import com.zch.api.dto.ask.CommentAnswerForm;
import com.zch.api.dto.user.ThumbForm;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.service.IAnswerService;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final IAnswerService answerService;

    /**
     * 查询某回答下的所有评论
     * @param id
     * @return
     */
    @GetMapping("/{id}/comments")
    public Response<List<CommentsVO>> getCommentsById(@PathVariable("id") Integer id) {
        return Response.success(answerService.getCommentsByAnswerId(id));
    }

    /**
     * 根据id删除评论
     * @param answerId 回答id
     * @param commentsId 评论id
     * @param type 评论类型
     * @return
     */
    @PostMapping("/{answerId}/comments/delete/{commentsId}")
    public Response<Boolean> deleteCommentsById(@PathVariable("answerId") Integer answerId,
                                                @PathVariable("commentsId") Integer commentsId,
                                                @RequestParam("type") String type) {
        return Response.success(answerService.deleteCommentsById(answerId, commentsId, type));
    }

    /**
     * 评论回答
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/{id}/comment")
    public Response<Boolean> commentAnswer(@PathVariable("id") Integer id, @RequestBody CommentAnswerForm form) {
        return Response.success(answerService.commentAnswer(id, form));
    }

    /**
     * 分页查找某个回答下的所有评论
     * @param id
     * @return
     */
    @GetMapping("/v2/{id}/comments")
    public PageResult<CommentsVO> getCommentsByAnswerId(@PathVariable("id") Integer id,
                                                        @RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(answerService.getCommentsPage(id, pageNum, pageSize));
    }

    /**
     * 点赞评论
     * @param form
     * @return
     */
    @PostMapping("/v2/thumb")
    public Response<Boolean> thumbAnswer(@RequestBody ThumbForm form) {
        return Response.success(answerService.thumbAnswer(form));
    }

}
