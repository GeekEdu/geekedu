package com.zch.label.domain.vo;

import com.zch.common.domain.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryPageVO extends BaseVO {

    private Long id;

    private String name;

}
