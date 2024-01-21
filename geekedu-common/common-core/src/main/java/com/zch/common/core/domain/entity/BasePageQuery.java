package com.zch.common.core.domain.entity;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@Data
public class BasePageQuery {

    private int pageNum = 1;

    private int pageSize = 10;

}
