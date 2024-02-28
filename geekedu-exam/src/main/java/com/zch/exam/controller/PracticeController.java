package com.zch.exam.controller;

import com.zch.api.dto.exam.ChapterForm;
import com.zch.api.dto.exam.DelChapterForm;
import com.zch.api.dto.exam.DelPracticeForm;
import com.zch.api.vo.exam.CTagsVO;
import com.zch.api.vo.exam.ChapterVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IPracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final IPracticeService practiceService;

    /**
     * 条件分页查找练习列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public PageResult getPracticeList(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam("sort") String sort,
                                  @RequestParam("order") String order,
                                  @RequestParam(value = "keywords", required = false) String keywords,
                                  @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return PageResult.success(practiceService.getPracticePage(pageNum, pageSize, sort, order, keywords, categoryId));
    }

    /**
     * 根据id获取练习明细
     * @param id
     * @return
     */
    @GetMapping("/getPracticeById/{id}")
    public Response getPracticeById(@PathVariable("id") Integer id) {
        return Response.success(practiceService.getPracticeById(id));
    }

    /**
     * 新增练习
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addPractice() {
        return Response.success();
    }

    /**
     * 更新明细
     * @param id
     * @return
     */
    @PostMapping("/update/{id}")
    public Response<Boolean> updatePractice(@PathVariable("id") Integer id) {
        return Response.success();
    }

    /**
     * 删除练习
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deletePractice(@PathVariable("id") Integer id) {
        return Response.success();
    }

    /**
     * 批量删除练习
     * @param form
     * @return
     */
    @PostMapping("/delete/batch")
    public Response<Boolean> deleteBatchPractice(@RequestBody DelPracticeForm form) {
        return Response.success();
    }

    /**
     * 返回分类列表
     * @return
     */
    @GetMapping("/tag/list")
    public Response<List<CTagsVO>> getTagList() {
        return Response.success(practiceService.getTagList());
    }

    /**
     * 返回章节列表
     * @param practiceId
     * @return
     */
    @GetMapping("/chapter/list")
    public Response<List<ChapterVO>> getChapterList(@RequestParam("pid") Integer practiceId) {
        return Response.success(practiceService.getChapterList(practiceId));
    }

    /**
     * 根据id查找章节明细
     * @param id
     * @return
     */
    @GetMapping("/chapter/getChapterById/{id}")
    public Response getChapterById(@PathVariable("id") Integer id) {
        return Response.success(practiceService.getChapterById(id));
    }

    /**
     * 新增章节
     * @param form
     * @return
     */
    @PostMapping("/chapter/add")
    public Response addChapter(@RequestBody ChapterForm form) {
        return Response.success(practiceService.addChapter(form));
    }

    /**
     * 批量删除章节
     * @param form
     * @return
     */
    @PostMapping("/chapter/delete/batch")
    public Response deleteBatchChapter(@RequestBody DelChapterForm form) {
        return Response.success(practiceService.deleteChapterBatch(form));
    }

    /**
     * 更新章节
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/chapter/update/{id}")
    public Response updateChapter(@PathVariable("id") Integer id, @RequestBody ChapterForm form) {
        return Response.success(practiceService.updateChapter(id, form));
    }

}
