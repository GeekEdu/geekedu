package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("certificate")
public class Certificate extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 证书名
     */
    private String name;

    /**
     * 证书参数
     */
    private String params;

    /**
     * 模版图片
     */
    private String templateImage;

    /**
     * 授予人数
     */
    private Integer userCount;

    @TableLogic
    private Boolean isDelete;

}
