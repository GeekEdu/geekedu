package com.zch.common.constants;

/**
 * @author Poison02
 * @date 2024/3/11
 */
public interface MQConstants {

    /**
     * 用户登录注册相关
     */
    public static final String USER_LOGIN_GROUP = "USER_LOGIN_GROUP";

    public static final String SEND_SMS_TOPIC = "SEND_SMS_TOPIC";

    /**
     * 保存数据库相关
     */
    public static final String SAVE_TO_MYSQL_GROUP = "SAVE_TO_MYSQL_GROUP";

    public static final String SAVE_TO_MYSQL_TOPIC = "SAVE_TO_MYSQL_TOPIC";

    /**
     * 创建订单相关
     */
    public static final String CRT_ORDER_GROUP = "CRT_ORDER_GROUP";

    public static final String CRT_ORDER_TOPIC = "CRT_ORDER_TOPIC";

    /**
     * 支付相关
     */
    public static final String PAY_GROUP = "PAY_GROUP";

    public static final String PAY_TOPIC = "PAY_TOPIC";

}
