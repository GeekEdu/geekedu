package com.zch.api.dto.course;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@Data
public class ChapterForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer sort;

    private Integer courseId;

}
