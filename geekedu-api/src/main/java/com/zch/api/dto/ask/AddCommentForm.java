package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/5
 */
@Data
public class AddCommentForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String content;

}
