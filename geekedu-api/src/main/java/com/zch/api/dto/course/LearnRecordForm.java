package com.zch.api.dto.course;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@Data
public class LearnRecordForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer videoId;

    private Integer duration;

}
