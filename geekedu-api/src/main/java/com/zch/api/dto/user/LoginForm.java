package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录表单实体
 * @author Poison02
 * @date 2024/1/12
 */
@Data
public class LoginForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String username;

    private String password;

    private String phone;

    /**
     * 真实验证码
     */
    private String imageCaptcha;

    /**
     * 某一验证码由后端固定生成的key
     */
    private String imageKey;

}
