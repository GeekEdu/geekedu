package com.zch.api.dto.resource;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/19
 */
@Data
public class VideoAddForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Double duration;

    private Long size;

    private String mediaSource;

    private String mediaId;

    private String coverLink;

    private String mediaName;

}
