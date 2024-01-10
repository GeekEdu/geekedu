package com.zch.api.dto.user;

import com.zch.common.constants.RegexConstants;
import com.zch.common.validate.annotation.EnumValid;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Data
public class UserDTO {

    private Long id;

    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "手机号格式错误")
    private String phone;

    private String name;

    @EnumValid(enumeration = {1,2,3}, message = "用户类型错误")
    @NotNull
    private Integer type;

    private Long roleId;

    private String avatar;

    private String intro;

    private String userName;

    @Email
    private String email;

    private String qq;

    private String ex;

    private String province;

    private String city;

    private String district;

    @EnumValid(enumeration = {0, 1}, message = "性别格式不正确")
    private Integer gender;
}
