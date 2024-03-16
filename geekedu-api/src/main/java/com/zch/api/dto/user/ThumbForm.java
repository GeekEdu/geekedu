package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@Data
public class ThumbForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    private String type;

}
