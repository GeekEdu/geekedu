package com.zch.api.dto.course.vod;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@Data
public class CourseSectionForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer courseId;

    private Long duration;

    private Long freeSeconds;

    private LocalDateTime groundingTime;

    private Boolean isShow;

    private String tencentVideoId;

    private String title;

    private Integer chapterId;

    private Boolean banDrag;

    private BigDecimal price;

    private Integer videoId;

}
