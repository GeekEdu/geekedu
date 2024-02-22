package com.zch.ask.controller;

import com.zch.api.dto.ask.QuestionDeleteBatchForm;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.ask.service.IQuestionService;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param pageNum 当前页
     * @param pageSize 每页记录数
     * @param sort 排序字段
     * @param order 排序方式 desc asc
     * @param keywords 标题关键字
     * @param userId 用户id
     * @param categoryId 分类id
     * @param status 问题状态 -1 || null 全部 0 未解决 1已解决
     * @param createdTime 时间区间 起始和结束
     * @return
     */
    @GetMapping("/getQuestionPage")
    public Response<QuestionAndCategoryVO> getQuestionPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("sort") String sort, @RequestParam("order") String order,
                                                           @RequestParam(value = "keywords", required = false) String keywords,
                                                           @RequestParam(value = "userId", required = false) String userId,
                                                           @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                           @RequestParam(value = "status", required = false) Integer status,
                                                           @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return Response.success(questionService.getQuestionPage(pageNum, pageSize, sort, order, keywords, userId, categoryId, status, createdTime));
    }

    /**
     * 批量删除问题
     * @param form
     * @return
     */
    @PostMapping("/deleteBatch")
    public Response<Boolean> deleteQuestionBatchIds(@RequestBody QuestionDeleteBatchForm form) {
        return Response.success(questionService.deleteQuestionBatchIds(form));
    }

    /**
     * 查找某个问题下的所有回答
     * @param id
     * @return
     */
    @GetMapping("/{id}/answers")
    public Response<List<AnswersVO>> getAnswersById(@PathVariable("id") Integer id) {
        return Response.success(questionService.getAnswersById(id));
    }

    /**
     * 根据id删除回答
     * @param questionId
     * @param answerId
     * @return
     */
    @PostMapping("/{questionId}/answer/delete/{answerId}")
    public Response<Boolean> deleteAnswerById(@PathVariable("questionId") Integer questionId,
                                              @PathVariable("answerId") Integer answerId) {
        return Response.success(questionService.deleteAnswerById(questionId, answerId));
    }

    /**
     * 将某个回答设置为正确答案
     * @param questionId
     * @param answerId
     * @return
     */
    @PostMapping("/{questionId}/answer/select/{answerId}")
    public Response<Boolean> setCorrectAnswer(@PathVariable("questionId") Integer questionId,
                                              @PathVariable("answerId") Integer answerId) {
        return Response.success(questionService.setCorrectAnswer(questionId, answerId));
    }

}
