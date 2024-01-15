package com.zch.common.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码生成器
 * @author Poison02
 * @date 2024/1/11
 */
public class CaptchaUtils {

    private final static String ENUM_STR = "0123456789qwertyuioplkjhgfdsazxcvbnmMNBVCXZLKJHGFDSAQWERTYUIOP";

    /**
     * 生成验证码
     * @return
     */
    public static Map<String, String> createPicCaptcha() {
        Map<String, String> result = new HashMap<>();
        // 参数分别是宽、高、验证码个数，横线干扰个数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(125, 48, 5, 20);
        // 生成四位数的验证码
        RandomGenerator randomGenerator = new RandomGenerator(ENUM_STR, 4);
        lineCaptcha.setGenerator(randomGenerator);
        // 数据
        String lineCaptchaCode = lineCaptcha.getCode();
        // base64
        String lineCaptchaBase64 = lineCaptcha.getImageBase64Data();
        // 生成 key
        String key = RandomUtils.randomString(128);
        System.out.println(lineCaptchaCode + " and " + lineCaptchaBase64);
        System.out.println("key: " + key);
        result.put("img", lineCaptchaBase64);
        result.put("key", key);
        result.put("code", lineCaptchaCode);
        return result;
    }

}
