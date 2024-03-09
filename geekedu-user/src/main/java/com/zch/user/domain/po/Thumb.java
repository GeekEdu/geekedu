package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.user.enums.ThumbEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@EqualsAndHashCode(callSuper = true)
@TableName("u_thumb")
@Data
public class Thumb extends BaseEntity {

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
    private ThumbEnums type;

    /**
     * 是否取消点赞
     */
    private Boolean isCancel;

}
