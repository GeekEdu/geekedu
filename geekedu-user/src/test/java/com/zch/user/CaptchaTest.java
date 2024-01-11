package com.zch.user;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import org.junit.jupiter.api.Test;

/**
 * @author Poison02
 * @date 2024/1/11
 */
public class CaptchaTest {

    private final static String TESTSTR = "0123456789qwertyuioplkjhgfdsazxcvbnmMNBVCXZLKJHGFDSAQWERTYUIOP";

    @Test
    public void testCode() {
        /**参数分别是宽、高、验证码个数，横线干扰个数**/
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(125, 48, 5, 20);
        RandomGenerator randomGenerator = new RandomGenerator(TESTSTR, 4);
        lineCaptcha.setGenerator(randomGenerator);
        String lineCaptchaCode = lineCaptcha.getCode();
        // 数据
        System.out.println(lineCaptchaCode);
        // base64编码
        System.out.println(lineCaptcha.getImageBase64Data());
    }

}
