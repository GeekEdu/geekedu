package com.zch.api.vo.label;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategorySimpleVO extends BaseVO {

    private Integer id;

    private String name;

}
