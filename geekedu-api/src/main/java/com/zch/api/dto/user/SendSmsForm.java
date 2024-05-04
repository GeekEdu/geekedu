package com.zch.api.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendSmsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码key
     */
    private String imageKey;

    /**
     * 图形验证码
     */
    private String imageCaptcha;

    /**
     * 场景 register login ...
     */
    private String scene;

}
