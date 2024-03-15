package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatVO extends BaseVO {

    /**
     * 类型 如 message
     */
    private String t;

    /**
     * 放消息id和消息内容
     * chat_id
     * content
     */
    private Map<String, Object> v;

    /**
     * 用于放当前发送信息的用户名
     */
    private Map<String, String> u;
}
