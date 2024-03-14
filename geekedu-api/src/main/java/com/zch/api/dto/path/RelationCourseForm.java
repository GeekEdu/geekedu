package com.zch.api.dto.path;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@Data
public class RelationCourseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private Integer relationId;

    private String name;

    private String cover;

    private String price;

    private String typeText;

}
