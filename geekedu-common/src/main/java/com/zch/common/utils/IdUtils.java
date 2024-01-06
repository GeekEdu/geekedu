package com.zch.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 基于 Hutool 雪花算法的id生成器
 * @author Poison02
 * @date 2024/1/6
 */
public class IdUtils {

    private static Snowflake snowflake = IdUtil.getSnowflake();

    /**
     * 生成long 类型的ID
     * @return
     */
    public static Long getId() {
        return snowflake.nextId();
    }

    /**
     * 生成String 类型的ID
     * @return
     */
    public static String getIdStr() {
        return snowflake.nextIdStr();
    }

    public static void main(String[] args) {
        System.out.println(IdUtils.getId());
        System.out.println(IdUtils.getIdStr());
    }

}
