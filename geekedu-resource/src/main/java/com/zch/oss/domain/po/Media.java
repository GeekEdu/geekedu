package com.zch.oss.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("media_resource")
public class Media extends BaseEntity implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件在云端的唯一标示，例如：387702302659783576
     */
    private String mediaId;

    /**
     * 文件名称
     */
    private String mediaName;

    /**
     * 媒体播放地址
     */
    private String mediaLink;

    /**
     * 媒体封面地址
     */
    private String coverLink;

    /**
     * 媒体来源 tencent aliyun
     */
    private String mediaSource;

    /**
     * 视频时长，单位秒
     */
    private Double duration;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 媒资大小，单位字节
     */
    private Long size;

    /**
     * 媒体大小，单位MB
     */
    private Double sizeMb;

    /**
     * 创建者
     */
    private Long createdBy;

    /**
     * 更新者
     */

    private Long updatedBy;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;
}
