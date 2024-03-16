package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.user.enums.CollectionEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("u_collection")
public class Collection extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关联id
     */
    private Integer relationId;

    /**
     * 点赞类型 图文 问答评论
     */
    private CollectionEnums type;

    /**
     * 是否取消点赞
     */
    private Boolean isCancel;

}
