package com.zch.api.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Data
public class LoginFormDTO {

    @NotNull
    private Integer type;

    private String userName;

    private String phone;

    @NotNull
    private String password;

    private Boolean rememberMe = false;

}
