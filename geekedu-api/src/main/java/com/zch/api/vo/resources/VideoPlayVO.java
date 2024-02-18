package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频播放VO
 * @author Poison02
 * @date 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoPlayVO extends BaseVO {

    /**
     * 视频文件id
     */
    private String mediaId;

    /**
     * 视频签名
     */
    private String signature;

}
