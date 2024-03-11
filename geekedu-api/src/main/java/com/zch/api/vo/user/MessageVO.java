package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageVO extends BaseVO {

    private String id;

    private Long userId;

    private String message;

    private LocalDateTime createdTime;

    private LocalDateTime readTime;

    private Boolean isRead;

}
