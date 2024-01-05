package com.zch.oss.domain.po;

// import com.baomidou.mybatisplus.annotation.TableField;
// import com.baomidou.mybatisplus.annotation.TableId;
// import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.oss.enums.FileStatus;
import com.zch.oss.enums.Platform;
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
// @TableName("file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，文件id
     */
    // @TableId(value = "id")
    private Long id;

    /**
     * 文件在云端的唯一标示，例如：aaa.jpg
     */
    // @TableField("`key_id`")
    private String keyId;

    /**
     * 文件上传时的名称
     */
    private String fileName;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 状态：1-待上传 2-已上传,未使用 3-已使用
     */
    private FileStatus status;

    /**
     * 状态：1-腾讯 2-阿里
     */
    private Platform platform;

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
