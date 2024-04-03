package com.zch.common.redis.constants;

/**
 * @author Poison02
 * @date 2024/1/12
 */
public interface RedisConstants {

    String LOGIN_USER_TOKEN = "login:user:token:";

    String AUTHORIZATION_LOGIN_TOKEN = "Authorization:login:token:";

    String AUTHORIZATION_LOGIN_SESSION = "Authorization:login:session:";

    String AUTHORIZATION_SAME_TOKEN = "Authorization:var:same-token";

    /**
     * token 过期时间 单位 秒
     */
    Long LOGIN_USER_TOKEN_TTL = 1800L;

    String CAPTCHA_KEY = "captcha:";

    String CAPTCHA_MAP = "captchaMap:";

    /**
     * 验证码过期时间 单位 秒
     */
    Long CAPTCHA_KEY_TTL = 60L;

    String SMS_CODE_KEY = "sms:";

    String PERMISSION_MAP = "permissionMap:";

    String ROLE_LIST = "roleList:";

    String USERINFO = "userInfo:";

    String USER_SIGN_KEY = "user:sign:";

    /**
     * 图文点赞集合
     */
    String IMAGE_TEXT_Z_SET = "topic:thumb:";

    /**
     * 问答评论点赞集合
     */
    String QA_COMMENT_Z_SET = "qa:comment:thumb:";

    /**
     * 电子书收藏集合
     */
    String E_BOOK_SET = "book:collection:";

    String IMAGE_TEXT_SET = "topic:collection:";

    String REPLAY_COURSE_SET = "vod:collection:";

    String LIVE_COURSE_SET = "live:collection:";

    /**
     * 限流 redis key
     */
    String RATE_LIMIT_KEY = "global:rate_limit:";

    String FRONTED_INDEX_DATA_KEY = "fronted:index:data";

}
