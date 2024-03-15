package com.zch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zch.common.mvc.result.Response;
import com.zch.domain.ChatVO;
import com.zch.domain.LiveDurationVO;
import com.zch.domain.WebSocket;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@RestController
@RequestMapping("/api/chat")
public class WebSocketController {

    @Resource
    private WebSocket webSocket;

    @GetMapping("/send")
    public String send(@RequestParam("msg") String msg) {
        ChatVO vo = new ChatVO();
        vo.setT("message");
        Map<String, Long> u = new HashMap<>(1);
        u.put("nick_name", 1745747394693820416L);
        vo.setU(u);
        Map<String, Object> v = new HashMap<>(1);
        v.put("chat_id", 1);
        v.put("content", msg);
        vo.setV(v);
        String message = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            message = objectMapper.writeValueAsString(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocket.sendMessage(message);
        return msg;
    }

    @PostMapping("/course/{courseId}/video/{videoId}/chat/send")
    public Response<Boolean> chatSendMsg(@PathVariable("courseId") Integer courseId,
                                         @PathVariable("videoId") Integer videoId,
                                         @RequestBody LiveDurationVO duration) {
        ChatVO vo = new ChatVO();
        vo.setT("message");
        Map<String, Long> u = new HashMap<>(1);
        u.put("nick_name", 1745747394693820416L);
        vo.setU(u);
        Map<String, Object> v = new HashMap<>(1);
        v.put("chat_id", 1);
        v.put("content", duration.getContent());
        vo.setV(v);
        String message = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            message = objectMapper.writeValueAsString(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocket.sendMessage(message);
        return Response.success();
    }

}
