package com.zch.api.vo.trade;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayChannelVO extends BaseVO {

    private Integer id;

    private String name;

    private String sign;

    private String image;

}
