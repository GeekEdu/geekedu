package com.zch.common.core.utils.encrypt;

import cn.hutool.crypto.digest.DigestUtil;
import com.zch.common.core.utils.RandomUtils;

/**
 * 密码工具类
 * @author Poison02
 * @date 2024/1/12
 */
public class EncryptUtils {

    /**
     * md5 加密
     * 注意在加密的时候需要传的是输入的密码以及盐值 这个盐值生成为：byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
     * @param password
     * @param key
     * @return
     */
    public static String md5Encrypt(String password, String key) {
        String md5Hex = DigestUtil.md5Hex(password + key);
        return md5Hex;
    }

    /**
     * 生成 盐值
     * @return
     */
    public static String generateKey() {
        return RandomUtils.randomString(24);
    }

    public static void main(String[] args) {
        String password = "123456";
        String key = generateKey();
        String s = md5Encrypt(password, key);
        System.out.println(s);
    }

}
