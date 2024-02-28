package com.zch.api.dto.exam;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Data
public class ChapterForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String name;

    private Integer practiceId;

    private Integer sort;
}
