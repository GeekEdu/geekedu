package com.zch.common.utils;

/**
 * @author Poison02
 * @date 2024/1/6
 */
public interface Convert<R, T> {

    void convert(R origin, T target);

}
