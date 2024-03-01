package com.zch.ask.controller;

import com.zch.api.dto.ask.QuestionDeleteBatchForm;
import com.zch.api.dto.ask.QuestionForm;
import com.zch.api.dto.ask.ReplyQuestionForm;
import com.zch.api.vo.ask.AnswersVO;
import com.zch.api.vo.ask.QuestionAndCategoryVO;
import com.zch.api.vo.ask.QuestionFullVO;
import com.zch.api.vo.label.CategorySimpleVO;
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

// =====================================================================================================
// 前台

    /**
     * 前台 返回问题列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    @GetMapping("/v2/list")
    public Response<QuestionAndCategoryVO> getV2Questions(@RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam("scene") String scene,
                                                          @RequestParam("categoryId") Integer categoryId) {
        return Response.success(questionService.getV2Questions(pageNum, pageSize, scene, categoryId));
    }

    /**
     * 返回问题-分类列表
     * @return
     */
    @GetMapping("/v2/category/list")
    public Response<List<CategorySimpleVO>> getV2QuestionTagList() {
        return Response.success(questionService.getTagList());
    }

    /**
     * 返回某个问题明细
     * @param id
     * @return
     */
    @GetMapping("/v2/detail/{id}")
    public Response<QuestionFullVO> getQuestionDetail(@PathVariable("id") Integer id) {
        return Response.success(questionService.getQuestionDetail(id));
    }

    /**
     * 新建问题
     * @return
     */
    @PostMapping("/v2/add")
    public Response<Integer> addQuestion(@RequestBody QuestionForm form) {
        return Response.success(questionService.addQuestion(form));
    }

    /**
     * 回答问题
     * @param form
     * @return
     */
    @PostMapping("/v2/{id}/answer")
    public Response<Boolean> replyQuestion(@PathVariable("id") Integer id, @RequestBody ReplyQuestionForm form) {
        return Response.success(questionService.replyQuestion(id, form));
    }

}
