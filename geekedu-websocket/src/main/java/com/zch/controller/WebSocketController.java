package com.zch.controller;

import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.common.mvc.result.Response;
import com.zch.service.IDanmuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class WebSocketController {

    private final IDanmuService danmuService;

    @PostMapping("/course/{courseId}/video/{videoId}/chat/send")
    public Response<Boolean> chatSendMsg(@PathVariable("courseId") Integer courseId,
                                         @PathVariable("videoId") Integer videoId,
                                         @RequestBody LiveDurationVO duration) {
        danmuService.saveDanmuInfo(courseId, videoId, duration);
        return Response.success(true);
    }

}
