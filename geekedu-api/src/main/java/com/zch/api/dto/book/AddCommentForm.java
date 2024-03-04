package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@Data
public class AddCommentForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 一级评论id
     */
    private Integer parentId;

    private String content;

    /**
     * 回复id
     */
    private Integer replyId;

}
