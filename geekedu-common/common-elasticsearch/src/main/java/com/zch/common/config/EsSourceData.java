package com.zch.common.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/21
 */
@Data
public class EsSourceData implements Serializable {

    private String docId;

    private Map<String, Object> data;

}
