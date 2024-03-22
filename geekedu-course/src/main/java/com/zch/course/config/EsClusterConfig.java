package com.zch.course.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/21
 */
@Data
public class EsClusterConfig implements Serializable {

    /**
     * 集群名称
     */
    private String name;

    /**
     * 集群节点
     */
    private String nodes;

}
