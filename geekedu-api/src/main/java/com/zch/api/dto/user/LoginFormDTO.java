package com.zch.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Data
@ApiModel(description = "登陆表单实体")
public class LoginFormDTO {

    @ApiModelProperty(value = "登录方式：1-密码登录，2-验证码登录", example = "1", required = true)
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "用户名", example = "jack")
    private String userName;

    @ApiModelProperty(value = "手机号", example = "13800010001")
    private String phone;

    @ApiModelProperty(value = "密码或者手机号验证码", example = "123456", required = true)
    @NotNull
    private String password;

    @ApiModelProperty(value = "7天免密登录", example = "true")
    private Boolean rememberMe = false;

}
