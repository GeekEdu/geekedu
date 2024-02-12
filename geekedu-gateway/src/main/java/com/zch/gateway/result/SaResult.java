package com.zch.gateway.result;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/12
 */
public class SaResult extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    public SaResult() {
    }

    public SaResult(int status, String message, Object data) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(data);
    }

    public SaResult(Map<String, ?> map) {
        this.setMap(map);
    }

    public Integer getCode() {
        return (Integer)this.get("status");
    }

    public String getMessage() {
        return (String)this.get("message");
    }

    public Object getData() {
        return this.get("data");
    }

    public SaResult setStatus(int code) {
        this.put("status", code);
        return this;
    }

    public SaResult setMessage(String message) {
        this.put("message", message);
        return this;
    }

    public SaResult setData(Object data) {
        this.put("data", data);
        return this;
    }

    public SaResult set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    //public <T> T get(String key, Class<T> cs) {
    //    return SaFoxUtil.getValueByType(this.get(key), cs);
    //}

    public SaResult setMap(Map<String, ?> map) {
        Iterator var2 = map.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            this.put(key, map.get(key));
        }

        return this;
    }

    public static SaResult ok() {
        return new SaResult(200, "ok", (Object)null);
    }

    public static SaResult ok(String message) {
        return new SaResult(200, message, (Object)null);
    }

    public static SaResult code(int code) {
        return new SaResult(code, (String)null, (Object)null);
    }

    public static SaResult data(Object data) {
        return new SaResult(200, "ok", data);
    }

    public static SaResult error() {
        return new SaResult(500, "error", (Object)null);
    }

    public static SaResult error(String message) {
        return new SaResult(500, message, (Object)null);
    }

    public static SaResult get(int code, String message, Object data) {
        return new SaResult(code, message, data);
    }

    public String toString() {
        return "{\"status\": " + this.getCode() + ", \"message\": " + this.transValue(this.getMessage()) + ", \"data\": " + this.transValue(this.getData()) + "}";
    }

    private String transValue(Object value) {
        return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
    }
}
