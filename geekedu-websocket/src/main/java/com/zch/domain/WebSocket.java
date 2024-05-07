package com.zch.domain;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.course.live.ChatVO;
import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.result.Response;
import com.zch.service.IDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Poison02
 * @date 2024/3/10
 */
//注册成组件
@Component
//定义websocket服务器端，它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址
@ServerEndpoint(value = "/live/course/{courseId}/video/{videoId}/token/{token}", subprotocols = {"protocol"})
@Slf4j
public class WebSocket {

    //实例一个session，这个session是websocket的session
    private Session session;

    /**
     * 注入 ApplicationContext 为了拿到Bean
     */
    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        WebSocket.context = context;
    }

    //存放websocket的集合
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private static final ConcurrentHashMap<Session, String> SESSION_MAP = new ConcurrentHashMap<>();

    //前端请求时一个websocket时
    @OnOpen
    public void onOpen(@PathParam("courseId") Integer courseId,
                       @PathParam("videoId") Integer videoId,
                       @PathParam("token") String token,
                       Session session) {
        this.session = session;
        // 校验当前token，看是否能拿到用户id
        if (StringUtils.isNotBlank(token)) {
            Long userId = Long.valueOf((String) StpUtil.getLoginIdByToken(token));
            // Long userId = 1745747394693820416L;
            // 看是否能查找到用户
            // 先拿到 Bean
            UserFeignClient userFeignClient = (UserFeignClient) context.getBean("userFeignClient");
            Response<UserSimpleVO> user = userFeignClient.getUserById(userId + "");
            // 将传入的参数做一个简单的加密，做为键
            // String key = generateUniqueKey(courseId, videoId, token);
            if (ObjectUtils.isNotNull(user) && ObjectUtils.isNotNull(user.getData())) {
                SESSION_MAP.put(session, token);
            }
            // 如果SESSION_MAP中有值且没有当前用户，则将当前用户放入Set中
            if (CollUtils.isNotEmpty(SESSION_MAP) && SESSION_MAP.containsKey(session)) {
                webSocketSet.add(this);
            }
        }
        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    //前端关闭时一个websocket时
    @OnClose
    public void onClose() {
        SESSION_MAP.clear();
        webSocketSet.remove(this);
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    //前端向后端发送消息
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
        ObjectMapper objectMapper = new ObjectMapper();
        Message message1;
        try {
            message1 = objectMapper.readValue(message, Message.class);
            LiveDurationVO vo = new LiveDurationVO();
            vo.setContent(message1.getContent());
            vo.setDuration(message1.getDuration());
            saveMsg(message1.getCourseId(), message1.getVideoId(), vo);
        } catch (Exception e) {

        }
    }

    //新增一个方法用于主动向客户端发送消息
    public static void sendMessage(String message) {
        for (WebSocket webSocket: webSocketSet) {
            log.info("【websocket消息】广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String generateUniqueKey(Integer courseId, Integer videoId, String token) {
        // 拼接参数并计算哈希
        String combined = courseId + "-" + videoId + "-" + token;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(combined.getBytes(StandardCharsets.UTF_8));

            // 使用Base64对哈希值进行URL安全且无填充的编码
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes).substring(0, 10);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法初始化SHA-256哈希函数", e);
        }
    }

    /**
     * 持久消息
     * @param courseId
     * @param videoId
     * @param duration
     */
    private void saveMsg(Integer courseId, Integer videoId, LiveDurationVO duration) {
        if (ObjectUtils.isNotNull(duration) && ObjectUtils.isNotNull(duration.getDuration())
                && StringUtils.isNotBlank(duration.getContent())) {
            String content = duration.getContent();
            BigDecimal seconds = duration.getDuration();
            // 通过拿到当前session的token，然后拿到当前用户id
            String token = SESSION_MAP.get(session);
            Long userId = Long.valueOf((String) StpUtil.getLoginIdByToken(token));
            // 构造信息发送
            UserFeignClient userFeignClient = (UserFeignClient) context.getBean("userFeignClient");
            UserSimpleVO user = userFeignClient.getUserById(userId + "").getData();
            ChatVO vo = new ChatVO();
            vo.setT("message");
            Map<String, String> u = new HashMap<>(1);
            u.put("nick_name", user.getName());
            Map<String, Object> v = new HashMap<>(2);
            v.put("chat_id", 1);
            v.put("content", content);
            vo.setU(u);
            vo.setV(v);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String message = objectMapper.writeValueAsString(vo);
                // 发送消息
                sendMessage(StringUtils.isEmpty(message) ? "" : message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 存数据库
            Danmu danmu = new Danmu();
            danmu.setContent(content);
            danmu.setDuration(seconds);
            danmu.setCourseId(courseId);
            danmu.setVideoId(videoId);
            danmu.setUserId(userId);
            IDanmuService danmuService = (IDanmuService) context.getBean("danmuService");
            danmuService.save(danmu);
        }
    }

}
