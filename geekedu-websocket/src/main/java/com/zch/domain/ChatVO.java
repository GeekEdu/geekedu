package com.zch.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/15
 */
public class ChatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String t;

    private Map<String, Object> v;

    private Map<String, Long> u;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Map<String, Object> getV() {
        return v;
    }

    public void setV(Map<String, Object> v) {
        this.v = v;
    }

    public Map<String, Long> getU() {
        return u;
    }

    public void setU(Map<String, Long> u) {
        this.u = u;
    }
}
