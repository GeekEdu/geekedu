package com.zch.api.vo.exam.practice;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryFirstVO extends BaseVO {

    private Integer id;

    private String name;

}
