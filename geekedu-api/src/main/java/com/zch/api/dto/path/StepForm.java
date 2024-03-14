package com.zch.api.dto.path;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@Data
public class StepForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String intro;

    private Integer sort;

    private Integer pathId;

    private List<RelationCourseForm> courses = new ArrayList<>(0);

}
