package com.zch.common.context;

/**
 * 使用 ThreadLocal 保存的用户信息
 * @author Poison02
 * @date 2023/12/28
 */
public class UserContext {

    private static final ThreadLocal<String> TL = new ThreadLocal<>();

    /**
     * 保存用户id
     * @param userId 用户id
     */
    public static void setUser(String userId) {
        TL.set(userId);
    }

    /**
     * 获取用户id
     * @return 用户id
     */
    public static String getUser() {
        return TL.get();
    }

    /**
     * 移除用户id
     */
    public static void removeUser() {
        TL.remove();
    }

}
