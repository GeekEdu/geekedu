package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/1
 */
@Data
public class CommentAnswerForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String content;

    private Long userId;

}
