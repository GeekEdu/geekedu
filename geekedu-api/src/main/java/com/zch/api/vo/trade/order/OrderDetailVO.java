package com.zch.api.vo.trade.order;

import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDetailVO extends BaseVO {

    private OrderFullVO order;

    private UserSimpleVO user;

}
