package com.zch.user.constants;

import java.time.Duration;

/**
 * @author Poison02
 * @date 2024/1/6
 */
public interface UserConstants {

    String DEFAULT_PASSWORD = "123456";

    Long ADMIN_ROLE_ID = 1L;

    String ADMIN_ROLE_NAME = "超级管理员";

    Long TEACHER_ROLE_ID = 2L;

    String TEACHER_ROLE_NAME = "教师";

    Long STUDENT_ROLE_ID = 3L;

    String STUDENT_ROLE_NAME = "学员";

    // 验证码的Redis key前缀
    String USER_VERIFY_CODE_KEY = "sms:user:code:phone:";

    // 验证码有效期，5分钟
    Duration USER_VERIFY_CODE_TTL = Duration.ofMinutes(5);

}
