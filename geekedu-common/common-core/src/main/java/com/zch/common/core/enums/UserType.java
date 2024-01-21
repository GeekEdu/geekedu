package com.zch.common.core.enums;

import com.zch.common.core.constants.ErrorInfo;
import com.zch.common.mvc.exception.BadRequestException;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Getter
public enum UserType implements BaseEnum{
    ADMIN(1, "超级管理员"),
    TEACHER(2, "教师"),
    STUDENT(3, "学员");

    int value;
    String desc;

    UserType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static UserType of(int value) {
        for (UserType type : UserType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new BadRequestException(ErrorInfo.Msg.INVALID_USER_TYPE);
    }
}
