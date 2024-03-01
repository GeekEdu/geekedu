package com.zch.api.dto.ask;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/2
 */
@Data
public class QuestionForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String title;

    private Integer categoryId;

    private String content;

    private List<String> images;

    private Integer rewardScore;

}
