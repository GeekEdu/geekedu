package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@Data
public class CollectForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String type;

}
