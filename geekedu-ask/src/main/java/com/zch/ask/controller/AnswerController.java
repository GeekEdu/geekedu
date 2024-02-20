package com.zch.ask.controller;

import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.service.IAnswerService;
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

}
