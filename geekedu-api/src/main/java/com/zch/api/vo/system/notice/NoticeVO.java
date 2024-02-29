package com.zch.api.vo.system.notice;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeVO extends BaseVO {

    private String title;

    private String announcement;

    private Long viewCount;

    private Long adminId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

}
