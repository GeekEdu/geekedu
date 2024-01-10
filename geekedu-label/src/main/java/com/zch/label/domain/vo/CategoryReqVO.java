package com.zch.label.domain.vo;

import com.zch.common.domain.vo.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryReqVO extends PageReqVO {

    public CategoryReqVO() {
        super();
    }

    private Long id;

    // 用于查询所有标签时的 标签id集合
    private List<Long> ids;
}
