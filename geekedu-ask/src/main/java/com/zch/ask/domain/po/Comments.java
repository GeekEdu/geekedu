package com.zch.ask.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.ask.enums.CommentsEnum;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("comments")
public class Comments extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关系id 如课程id、课时id等
     */
    private Integer relationId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 回答id
     */
    private Integer answerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论类型
     */
    private CommentsEnum cType;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
