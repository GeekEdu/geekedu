package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@Data
public class CommentsForm implements Serializable {

    private static final Long serialVersionUID = 1L;

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
    private String cType;

}
