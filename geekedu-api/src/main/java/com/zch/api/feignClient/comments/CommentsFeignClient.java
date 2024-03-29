package com.zch.api.feignClient.comments;

import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.ask.CommentsVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@FeignClient(contextId = "ask", value = "ask-service", configuration = FeignInterceptor.class)
public interface CommentsFeignClient {

    @GetMapping("/api/comments/getCommentsPage")
    public PageResult<CommentsVO> getCommentsPage(@RequestParam("pageNum") Integer pageNum,
                                                  @RequestParam("pageSize") Integer pageSize,
                                                  @RequestParam("cType") String cType,
                                                  @RequestParam(value = "createdTime", required = false) List<String> createdTime);

    @PostMapping("/api/comments/delete/batch")
    public Response<Boolean> deleteBatchComments(@RequestBody CommentsBatchDelForm form);

    /**
     * 获取评论列表
     * @param id
     * @param cType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/api/comments/v2/list")
    public Response<CommentsFullVO> getCommentsList(@RequestParam("id") Integer id,
                                                    @RequestParam("cType") String cType,
                                                    @RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer pageSize);

    /**
     * 前台 新增评论
     * @param relationId
     * @param form
     * @param cType
     * @return
     */
    @PostMapping("/api/comments/v2/add")
    public Response<Boolean> addComment(@RequestParam("id") Integer relationId,
                                        @RequestParam("cType") String cType,
                                        @RequestBody AddCommentForm form);

}
