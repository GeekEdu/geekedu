package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/1
 */
@Data
public class ReplyQuestionForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String content;

    private List<String> images;

}
