package com.zch.common.context;

/**
 * 使用 ThreadLocal 保存的用户信息
 * @author Poison02
 * @date 2023/12/28
 */
public class UserContext {

    private static final ThreadLocal<Long> TL = new ThreadLocal<>();

    /**
     * 保存用户id
     * @param userId 用户id
     */
    public static void setUser(Long userId) {
        TL.set(userId);
    }

    /**
     * 获取用户id
     * @return 用户id
     */
    public static Long getUser() {
        return TL.get();
    }

    /**
     * 移除用户id
     */
    public static void removeUser() {
        TL.remove();
    }

}
