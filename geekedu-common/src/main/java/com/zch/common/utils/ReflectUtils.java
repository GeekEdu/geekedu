package com.zch.common.utils;

import cn.hutool.core.util.ReflectUtil;

/**
 * 反射工具
 * @author Poison02
 * @date 2023/12/28
 */
public class ReflectUtils extends ReflectUtil {

    /**
     * 判断一个类中是否含有指定字段
     * @param fieldName
     * @param clazz
     * @return
     */
    public static boolean containField(String fieldName, Class<?> clazz) {
        return getField(clazz, fieldName) != null;
    }

}
