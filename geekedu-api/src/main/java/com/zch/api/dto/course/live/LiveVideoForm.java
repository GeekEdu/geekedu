package com.zch.api.dto.course.live;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Data
public class LiveVideoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private Boolean isShow;

    private LocalDateTime liveTime;

    private Long estimateDuration;

    private Integer chapterId;

    private Integer courseId;

}
