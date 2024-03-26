package com.zch.api.dto.trade.seckill;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/26
 */
@Data
public class CaptchaForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码key
     */
    private String captchaKey;

}
