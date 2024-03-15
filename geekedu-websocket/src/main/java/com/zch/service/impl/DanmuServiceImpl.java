package com.zch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.course.live.ChatVO;
import com.zch.api.vo.course.live.LiveDurationVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.domain.Danmu;
import com.zch.domain.WebSocket;
import com.zch.mapper.DanmuMapper;
import com.zch.service.IDanmuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu> implements IDanmuService {

    private final WebSocket webSocket;

    private final UserFeignClient userFeignClient;

    @Override
    public void saveDanmuInfo(Integer courseId, Integer videoId, LiveDurationVO duration) {

        if (ObjectUtils.isNotNull(duration) && ObjectUtils.isNotNull(duration.getDuration())
                && StringUtils.isNotBlank(duration.getContent())) {
            String content = duration.getContent();
            BigDecimal seconds = duration.getDuration();
            // Long userId = UserContext.getLoginId();
            Long userId = 1745747394693820416L;
            // 构造信息发送
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
                webSocket.sendMessage(StringUtils.isEmpty(message) ? "" : message);
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
            save(danmu);
        }
    }

}
