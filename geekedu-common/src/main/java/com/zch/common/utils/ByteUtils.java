package com.zch.common.utils;

import cn.hutool.core.util.ByteUtil;

/**
 * 字节工具类
 * @author Poison02
 * @date 2023/12/28
 */
public class ByteUtils extends ByteUtil {

    /**
     * 将字节数组转换为 String，为空则返回空串
     * @param content 字节数组
     * @return String
     */
    public static String parse(byte[] content) {
        if (content == null || content.length == 0) {
            return StringUtils.EMPTY;
        }
        return new String(content);
    }

}
