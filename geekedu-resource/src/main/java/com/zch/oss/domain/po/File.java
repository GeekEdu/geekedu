package com.zch.oss.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.oss.enums.FileFromEnum;
import com.zch.oss.enums.Platform;
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
@TableName("file_resource")
public class File extends BaseEntity implements Serializable{

    /**
     * 主键，文件id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 文件在云端的唯一标示，例如：aaa.jpg
     */
    @TableField("`key_id`")
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
     * 图片上传来源 0-全部，1-ppt，2-课程封面，3-课程详情页，4-文章配图
     */
    private FileFromEnum fileFrom;

    /**
     * 图片在云端的路径 不带 url 的
     */
    private String filePath;

    /**
     * 状态：1-腾讯 2-阿里
     */
    private Platform platform;

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
