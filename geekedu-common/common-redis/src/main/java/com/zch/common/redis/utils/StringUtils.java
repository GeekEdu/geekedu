package com.zch.common.redis.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * 字符串工具类 继承自 StrUtil
 * @author Poison02
 * @date 2023/12/28
 */
public class StringUtils extends StrUtil {

    /**
     * 通过 大写转小写 判断两个字符串是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isSameStringByUpperToLower(String str1, String str2) {
        return Objects.equals(str1.toLowerCase(), str2.toLowerCase());
    }

}
