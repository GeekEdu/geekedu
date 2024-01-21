package com.zch.common.core.domain.dto;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Data
public class LoginUserDTO {

    private Long userId;

    private Long roleId;

    private Boolean rememberMe;

}
