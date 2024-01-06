package com.zch.common.enums;

/**
 * @author Poison02
 * @date 2024/1/6
 */
public interface BaseEnum {

    int getValue();

    String getDesc();

    default boolean equalsValue(Integer value) {
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }

}
