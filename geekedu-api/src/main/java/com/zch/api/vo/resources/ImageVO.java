package com.zch.api.vo.resources;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageVO extends BaseVO {

    /**
     * 图片id
     */
    private Integer id;

    /**
     * 图片从哪儿上传
     */
    private Integer from;

    /**
     * 图片链接
     */
    private String url;

    /**
     * 图片路径
     */
    private String path;

    /**
     * 图片来源 cos oos minio
     */
    private String source;

    /**
     * 图片名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

}
