package com.zch.common.constants;

/**
 * @author Poison02
 * @date 2024/1/12
 */
public interface RedisConstants {

    String LOGIN_USER_TOKEN = "login:user:token:";

    /**
     * token 过期时间 单位 秒
     */
    Long LOGIN_USER_TOKEN_TTL = 1800L;

    String CAPTCHA_KEY = "captcha:";

    String CAPTCHA_MAP = "captchaMap";

    /**
     * 验证码过期时间 单位 秒
     */
    Long CAPTCHA_KEY_TTL = 60L;

}
