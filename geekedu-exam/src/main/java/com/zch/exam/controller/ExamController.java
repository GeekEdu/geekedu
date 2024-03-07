package com.zch.exam.controller;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.practice.PracticeDetailVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ExamController {

    private final IExamService examService;

    /**
     * 前台 返回考试首页 数量
     * @return
     */
    @GetMapping("/count/list")
    public Response<ExamCountVO> getExamCount() {
        return Response.success(examService.getExamCount());
    }

    /**
     * 前台 练习模式 列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    @GetMapping("/practice/list")
    public Response<PracticeFrontVO> getPracticeList(@RequestParam("pageNum") Integer pageNum,
                                                     @RequestParam("pageSize") Integer pageSize,
                                                     @RequestParam("cid") Integer categoryId,
                                                     @RequestParam("childId") Integer childId) {
        return Response.success(examService.getPracticeList(pageNum, pageSize, categoryId, childId));
    }

    /**
     * 前台 练习明细
     * @param id
     * @return
     */
    @GetMapping("/practice/detail/{id}")
    public Response<PracticeDetailVO> getPracticeDetail(@PathVariable("id") Integer id) {
        return Response.success(examService.getPracticeDetailById(id));
    }

    /**
     * 在线考试列表 在线考试就是后台的试卷！
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    @GetMapping("/paper/list")
    public Response getPaperList(@RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("cid") Integer categoryId,
                                 @RequestParam("childId") Integer childId) {
        return Response.success(examService.getPaperList(pageNum, pageSize, categoryId, childId));
    }

    /**
     * 模拟考试列表 即后台的模拟！
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param childId
     * @return
     */
    @GetMapping("/mock/list")
    public Response getMockList(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("cid") Integer categoryId,
                                @RequestParam("childId") Integer childId) {
        return Response.success(examService.getMockList(pageNum, pageSize, categoryId, childId));
    }

    /**
     * 错题本列表 后台无
     * @return
     */
    @GetMapping("/wrongBook/list")
    public Response getWrongBookList() {
        return Response.success();
    }

    /**
     * 收藏试题列表 后台无
     * @return
     */
    @GetMapping("/collection/list")
    public Response getCollectionList() {
        return Response.success();
    }

}
