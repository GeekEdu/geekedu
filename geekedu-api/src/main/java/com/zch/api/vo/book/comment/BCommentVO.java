package com.zch.api.vo.book.comment;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BCommentVO extends BaseVO {

    private Integer id;

    private String content;

    private Long userId;

    /**
     * 父级id，用于一个评论下有多个评论 即一级评论的id 默认 0
     */
    private Integer parentId;

    /**
     * 关联id
     */
    private Integer relationId;

    /**
     * 子评论数量
     */
    private Long childrenCount;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private UserSimpleVO user;

}
