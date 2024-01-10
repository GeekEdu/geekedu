package com.zch.oss.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MediaVO {

    private Long id;

    private String mediaName;

    private String coverLink;

    private Float duration;

    private Long size;

    private Integer useTimes;

    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

}
