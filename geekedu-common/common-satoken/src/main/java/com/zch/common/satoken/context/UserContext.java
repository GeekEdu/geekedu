package com.zch.common.satoken.context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用 ThreadLocal 保存的用户信息
 * @author Poison02
 * @date 2023/12/28
 */
public class UserContext {

    private static final InheritableThreadLocal<Map<String, Long>> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(String key, Long val) {
        Map<String, Long> map = getThreadLocalMap();
        map.put(key, val);
    }

    public static Long get(String key) {
        Map<String, Long> map = getThreadLocalMap();
        return map.get(key);
    }

    public static Long getLoginId() {
        return getThreadLocalMap().get("loginId");
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static Map<String, Long> getThreadLocalMap() {
        Map<String, Long> map = THREAD_LOCAL.get();
        if (Objects.isNull(map)) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

}
