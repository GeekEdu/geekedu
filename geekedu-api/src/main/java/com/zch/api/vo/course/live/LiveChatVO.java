package com.zch.api.vo.course.live;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveChatVO extends BaseVO {

    private String connect_url = "";

    private String channel = "";

    private UserSimpleVO user;

}
