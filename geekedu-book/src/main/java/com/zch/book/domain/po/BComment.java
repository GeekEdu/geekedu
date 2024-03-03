package com.zch.book.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.book.enums.CommentEnums;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("b_comment")
public class BComment extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Long userId;

    /**
     * 父级id，用于一个评论下有多个评论 即一级评论的id 默认 0
     */
    private Integer parentId;

    /**
     * 回复哪个评论，它的id
     */
    private Integer replyId;

    /**
     * 关联id
     */
    private Integer relationId;

    /**
     * 子评论数量
     */
    private Long childrenCount;

    /**
     * 评论类型 1-图文 2-电子书 3-电子书文章
     */
    private CommentEnums cType;

    @TableLogic
    private Boolean isDelete;

}
