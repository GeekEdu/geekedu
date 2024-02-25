package com.zch.exam.controller;

import com.zch.api.dto.exam.DeleteBatchQuestions;
import com.zch.api.dto.exam.TagForm;
import com.zch.api.vo.exam.QuestionsFullVO;
import com.zch.api.vo.exam.TagsVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IQuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionsController {

    private final IQuestionsService questionsService;

    /**
     * 返回题库列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param type
     * @param level
     * @return
     */
    @GetMapping("/getQuestionsPage")
    public Response<QuestionsFullVO> getQuestionsPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                      @RequestParam(value = "type", required = false) Integer type,
                                                      @RequestParam(value = "level", required = false) Integer level) {
        return Response.success(questionsService.getQuestionPage(pageNum, pageSize, categoryId, type, level));
    }

    /**
     * 批量删除题库
     * @param form
     * @return
     */
    @PostMapping("/deleteBatch")
    public Response<Boolean> deleteQuestionsBatch(@RequestBody DeleteBatchQuestions form) {
        return Response.success(questionsService.deleteBatchQuestions(form));
    }

    /**
     * 返回题目数据中的类型、分类、等级
     * @return
     */
    @GetMapping("/questionTypeList")
    public Response<QuestionsFullVO> getQuestionsTypeList() {
        return Response.success(questionsService.getQuestionsTypeList());
    }

    /**
     * 分类列表
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    @GetMapping("/category/list")
    public PageResult<TagsVO> getCategoryList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("type") String type) {
        return PageResult.success(questionsService.getCategoryList(pageNum, pageSize, type));
    }

    /**
     * 新增分类
     * @return
     */
    @PostMapping("/category/add")
    public Response<Boolean> addCategory(@RequestBody TagForm form) {
        return Response.success(questionsService.addCategory(form));
    }

    /**
     * 删除分类
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/category/delete/{id}")
    public Response<Boolean> deleteCategoryById(@PathVariable("id") Integer id, @RequestParam("type") String type) {
        return Response.success(questionsService.deleteCategoryById(id, type));
    }

    /**
     * 更新分类
     * @param id
     * @return
     */
    @PostMapping("/category/update/{id}")
    public Response<Boolean> updateCategory(@PathVariable("id") Integer id, @RequestBody TagForm form) {
        return Response.success(questionsService.updateCategory(id, form));
    }

    /**
     * 根据id，type查看分类明细
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/category/{id}")
    public Response<TagsVO> getCategoryById(@PathVariable("id") Integer id, @RequestParam("type") String type) {
        return Response.success(questionsService.getCategoryById(id, type));
    }

}
