package com.zch.media.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@ApiModel(description = "媒体上传的结果")
public class MediaUploadResultDTO {

    @ApiModelProperty(value = "文件在云端的唯一标示", example = "387702302659783576")
    private String mediaId;
/*
    @ApiModelProperty(value = "媒体播放地址", example = "http://xxx.mp4")
    private String mediaUrl;

    @ApiModelProperty(value = "媒体封面地址", example = "http://xxx.jpg")
    private String coverUrl;

    @ApiModelProperty(value = "文件名称", example = "Redis最佳实践.mp4")
    // TODO 限制文件名长度
    private String filename;*/
}
