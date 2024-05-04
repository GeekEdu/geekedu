package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/4/1
 */
@Data
public class RegForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String phone;

    private String password;

    private String phoneCode;

    private String scene;

}
