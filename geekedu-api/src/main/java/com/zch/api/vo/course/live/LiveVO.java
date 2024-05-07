package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveVO extends BaseVO {

    private Integer record_exists = 0;

    private Integer record_duration = 0;

    private LiveVideoVO video;

    private LiveCourseVO course;

    private String play_url = "";

    private String web_rtc_play_url = "";

    private String ali_rts = "";

    private String tencent_web_rtc = "";

    private LiveChatVO chat;

    private Integer room_is_ban = 0;

    private Integer user_is_ban = 0;

    private List<Object> sign_in_record = new ArrayList<>(0);

    private String liveUrl = "";

}
