package com.zch.api.vo.user;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VipVO extends BaseVO {

    private Integer id;

    private String name;

    private String intro;

    private Integer day;

    private BigDecimal price;

}
