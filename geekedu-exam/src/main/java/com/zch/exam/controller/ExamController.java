package com.zch.exam.controller;

import com.zch.api.vo.exam.ExamCountVO;
import com.zch.common.mvc.result.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class ExamController {

    /**
     * 前台 返回考试首页 数量
     * @return
     */
    @GetMapping("/count/list")
    public Response<ExamCountVO> getExamCount() {
        return Response.success(new ExamCountVO());
    }

}
