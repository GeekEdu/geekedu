package com.zch.ask.controller;

import com.zch.ask.service.IQuestionService;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/2/15
 */
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final IQuestionService questionService;

    /**
     * 分页查找问题列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param userId
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/getQuestionPage")
    public Response getQuestionPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("sort") String sort, @RequestParam("order") String order,
                                    @RequestParam("keywords") String keywords, @RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "status", required = false) Integer status) {
        return Response.success(questionService.getQuestionPage(pageNum, pageSize, sort, order, keywords, userId, categoryId, status));
    }

}
