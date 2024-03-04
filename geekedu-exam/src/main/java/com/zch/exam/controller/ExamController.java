package com.zch.exam.controller;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.api.vo.exam.practice.PracticeFrontVO;
import com.zch.common.mvc.result.Response;
import com.zch.exam.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
