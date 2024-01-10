package com.zch.common.domain.vo;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@Data
public class PageReqVO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
