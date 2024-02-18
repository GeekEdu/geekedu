package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoVO extends BaseVO {

    /**
     * 视频在数据库的id
     */
    private Integer id;

    /**
     * 视频id
     */
    private String mediaId;

    /**
     * 视频名称
     */
    private String mediaName;

    /**
     * 视频封面地址
     */
    private String coverLink;

    /**
     * 视频上传来源
     */
    private String mediaSource;

    /**
     * 视频时长 秒
     */
    private Double duration;

    /**
     * 视频大小 字节
     */
    private Long size;

    /**
     * 视频大小 MB
     */
    private Double sizeMb;

    /**
     * 视频创建时间
     */
    private LocalDateTime createdTime;

}
