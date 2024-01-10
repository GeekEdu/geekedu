package com.zch.oss.domain.dto;

import lombok.Data;

@Data
public class MediaDTO {
    private Long id;

    private String mediaName;

    private Float duration;

    private Long size;
}
