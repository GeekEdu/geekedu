package com.zch.user.domain.form;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Data
public class LoginForm {

    private String username;

    private String password;

    /**
     * 前端输入的验证码
     */
    private String imageCaptcha;

    /**
     * 后端返回给前端的验证码key
     */
    private String imageKey;

}
