package com.zch.media.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@ApiModel(description = "视频播放的签名信息")
public class VideoPlayVO {

    @ApiModelProperty(value = "视频唯一标示", example = "12412534535143242")
    private String mediaId;

    @ApiModelProperty(value = "视频封面", example = "xxx.xxx.xxx")
    private String signature;
}
