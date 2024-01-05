package com.zch.oss.domain.po;

// import com.baomidou.mybatisplus.annotation.TableId;
// import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.oss.enums.FileStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
// @TableName("media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    // @TableId(value = "id")
    private Long id;

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
     * 视频时长，单位秒
     */
    private Float duration;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 状态：1-上传中，2-已上传
     */
    private FileStatus status;

    /**
     * 媒资大小，单位字节
     */
    private Long size;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */

    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    private Integer isDelete;
}
