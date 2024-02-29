package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/29
 */
@Data
public class VipForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String name;

    private String price;

    private Integer day;

    private String intro;
}
