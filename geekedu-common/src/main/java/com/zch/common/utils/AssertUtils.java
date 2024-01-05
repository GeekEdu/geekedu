package com.zch.common.utils;

import com.zch.common.constants.ErrorInfo;
import com.zch.common.exceptions.BadRequestException;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

/**
 * 断言工具类
 * @author Poison02
 * @date 2023/12/31
 */
public class AssertUtils {

    public static void equals(Object obj1, Object obj2, String ... message){
        if (obj1 == null || obj2 == null) {
            handleException(message);
            return;
        }
        if (obj1 == obj2) {
            return;
        }
        if(!obj1.equals(obj2)){
            handleException(message);
        }
    }
    public static void isNotNull(Object obj, String ... message){
        if (obj == null) {
            handleException(message);
        }
    }

    public static void isNotBlank(String str, String ... message){
        if (StringUtils.isBlank(str)) {
            handleException(message);
        }
    }

    public static void isTrue(Boolean boo, String... message) {
        if (BooleanUtils.isFalse(boo)) {
            handleException(message);
        }
    }

    public static void isFalse(Boolean boo, String... message) {
        if (BooleanUtils.isTrue(boo)) {
            handleException(message);
        }
    }

    private static void handleException(String ... message){
        String msg = ErrorInfo.Msg.REQUEST_PARAM_ILLEGAL;
        if(message != null && message.length > 0){
            msg = message[0];
        }
        throw new BadRequestException(msg);
    }

    public static void isNotEmpty(Iterable<?> coll, String ... message) {
        if(CollUtils.isEmpty(coll)){
            handleException(message);
        }
    }
    public static void isNotEmpty(Map<?, ?> map, String ... message) {
        if(CollUtils.isEmpty(map)){
            handleException(message);
        }
    }

}