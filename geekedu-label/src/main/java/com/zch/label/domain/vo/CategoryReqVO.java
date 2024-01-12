package com.zch.label.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@Data
public class CategoryReqVO {

    private Long id;

    // 用于查询所有标签时的 标签id集合
    private List<Long> ids;
}
