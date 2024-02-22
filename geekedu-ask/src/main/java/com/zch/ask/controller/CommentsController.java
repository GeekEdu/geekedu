package com.zch.ask.controller;

import com.zch.api.vo.ask.CommentsVO;
import com.zch.ask.service.ICommentsService;
import com.zch.common.mvc.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
