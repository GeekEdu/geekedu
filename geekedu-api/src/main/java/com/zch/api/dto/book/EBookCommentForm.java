package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/4
 */
@Data
public class EBookCommentForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String content;

}
