package com.zch.common.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/6
 */
public class BeanUtils extends BeanUtil {

    /**
     * 将原对象转换成目标对象，对于字段不匹配的字段可以使用转换器处理
     *
     * @param source  原对象
     * @param clazz   目标对象的class
     * @param convert 转换器
     * @param <R>     原对象类型
     * @param <T>     目标对象类型
     * @return 目标对象
     */
    public static <R, T> T copyBean(R source, Class<T> clazz, Convert<R, T> convert) {
        T target = copyBean(source, clazz);
        if (convert != null) {
            convert.convert(source, target);
        }
        return target;
    }
    /**
     * 将原对象转换成目标对象，对于字段不匹配的字段可以使用转换器处理
     *
     * @param source  原对象
     * @param clazz   目标对象的class
     * @param <R>     原对象类型
     * @param <T>     目标对象类型
     * @return 目标对象
     */
    public static <R, T> T copyBean(R source, Class<T> clazz){
        if (source == null) {
            return null;
        }
        return toBean(source, clazz);
    }

    public static <R, T> List<T> copyList(List<R> list, Class<T> clazz) {
        if (list == null || list.isEmpty()) {
            return CollUtils.emptyList();
        }
        return copyToList(list, clazz);
    }

    public static <R, T> List<T> copyList(List<R> list, Class<T> clazz, Convert<R, T> convert) {
        if (list == null || list.isEmpty()) {
            return CollUtils.emptyList();
        }
        return list.stream().map(r -> copyBean(r, clazz, convert)).collect(Collectors.toList());
    }

}
