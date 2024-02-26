package com.zch.ask.controller;

import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.ask.CommentsForm;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.service.ICommentsService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final ICommentsService commentsService;

    /**
     * 条件分页查评论列表
     * @param pageNum
     * @param pageSize
     * @param createdTime
     * @return
     */
    @GetMapping("/getCommentsPage")
    public PageResult<CommentsVO> getCommentsPage(@RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize,
                                                  @RequestParam("cType") String cType,
                                                  @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return PageResult.success(commentsService.getCommentsPage(pageNum, pageSize, cType, createdTime));
    }

    /**
     * 根据id删除评论
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response deleteByIdAndType(@PathVariable("id") Integer id, @RequestParam("cType") String cType) {
        return Response.success(commentsService.deleteComments(id, cType));
    }

    /**
     * 批量删除评论
     * @param form
     * @return
     */
    @PostMapping("/delete/batch")
    public Response<Boolean> deleteBatchComments(@RequestBody CommentsBatchDelForm form) {
        return Response.success(commentsService.deleteCommentsBatch(form));
    }

    /**
     * 新增评论
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response addComments(CommentsForm form) {
        return Response.success();
    }

}
