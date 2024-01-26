package com.zch.common.mvc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/1/26
 */
@Data
public class BasePageQuery implements Serializable {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
