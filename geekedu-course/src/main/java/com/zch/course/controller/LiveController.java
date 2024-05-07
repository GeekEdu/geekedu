package com.zch.course.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.api.vo.course.live.LiveVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.course.service.ILiveCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@RestController
@RequestMapping("/api/live")
@RequiredArgsConstructor
public class LiveController {

    private final ILiveCourseService liveCourseService;

    @GetMapping("/v2/{id}/play")
    public Response<LiveVO> getPlayInfo(@PathVariable("id") Integer id) {
        return Response.success(liveCourseService.getPlayInfo(id));
    }

    @PostMapping("/course/{courseId}/video/{videoId}/liveWatchRecord")
    public Response<Boolean> liveWatchRecord(@PathVariable("courseId") Integer courseId,
                                             @PathVariable("videoId") Integer videoId,
                                             @RequestBody LiveDurationVO duration) {
        return Response.success(liveCourseService.liveWatchRecord(courseId, videoId, duration));
    }

    @GetMapping("/course/{courseId}/video/{videoId}/chat/msg")
    public PageResult getChatMsg(@PathVariable("courseId") Integer courseId,
                                 @PathVariable("videoId") Integer videoId,
                                 @RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize) {
        return PageResult.success(new Page<>());
    }

}
