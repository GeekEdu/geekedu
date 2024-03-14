package com.zch.course.controller;

import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.api.vo.course.live.LiveVO;
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

//    @RequestMapping("/index")
//    public ModelAndView getLiveIndex() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("index");
//        return mv;
//    }

    @GetMapping("/v2/{id}/play")
    public Response<LiveVO> getPlayInfo(@PathVariable("id") Integer id) {
        return Response.success(liveCourseService.getPlayInfo(id));
    }

    @PostMapping("/course/{courseId}/video/{videoId}/liveWatchRecord")
    public Response<Boolean> liveWatchRecord(@PathVariable("courseId") Integer courseId,
                                             @PathVariable("videoId") Integer videoId,
                                             @RequestBody LiveDurationVO duration) {
        return Response.success();
    }

    @PostMapping("/course/{courseId}/video/{videoId}/chat/send")
    public Response<Boolean> chatSendMsg(@PathVariable("courseId") Integer courseId,
                                         @PathVariable("videoId") Integer videoId,
                                         @RequestBody LiveDurationVO duration) {
        return Response.success();
    }

}
