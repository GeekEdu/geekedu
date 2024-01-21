package com.zch.user.enums;

import com.zch.common.mvc.exception.BadRequestException;
import com.zch.user.constants.UserErrorInfo;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Getter
public enum UserStatus {

    FROZEN(0, "禁止使用"),
    NORMAL(1, "已激活"),
    ;

    int value;
    String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserStatus of (int value) {
        if (value == 0) {
            return FROZEN;
        }
        if (value == 1) {
            return NORMAL;
        }
        throw new BadRequestException(UserErrorInfo.Msg.INVALID_USER_STATUS);
    }
}
