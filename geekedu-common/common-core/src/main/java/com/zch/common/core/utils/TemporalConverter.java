package com.zch.common.core.utils;

import cn.hutool.core.convert.impl.TemporalAccessorConverter;

import java.time.temporal.TemporalAccessor;

/**
 * @author Poison02
 * @date 2023/12/28
 */
public class TemporalConverter extends TemporalAccessorConverter {

    public TemporalConverter(Class<?> targetType) {
        super(targetType);
    }

    public TemporalConverter(Class<?> targetType, String format) {
        super(targetType, format);
    }

    @Override
    protected TemporalAccessor convertInternal(Object value) {
        if (value instanceof String) {
            String val = value.toString();
            try {
                return super.convertInternal(Long.parseLong(val));
            } catch (NumberFormatException e) {
                return super.convertInternal(value);
            }
        }
        return super.convertInternal(value);
    }

}
