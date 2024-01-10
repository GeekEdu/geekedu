package com.zch.common.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/8
 */
@Data
public class PageVO<T> {

    private long total;

    private Integer pageNum;

    private Integer pageSize;

    private Integer pageCount;

    private List<T> list;

}
