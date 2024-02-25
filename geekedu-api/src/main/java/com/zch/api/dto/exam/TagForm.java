package com.zch.api.dto.exam;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Data
public class TagForm implements Serializable {

    private final static long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Integer sort;

    private String type;

    private Integer parentId;

}
