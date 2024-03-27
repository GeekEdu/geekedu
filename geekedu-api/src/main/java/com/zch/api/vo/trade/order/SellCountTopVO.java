package com.zch.api.vo.trade.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SellCountTopVO extends BaseVO {

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 支付订单数
     */
    private Long orderCount;

    /**
     * 支付总金额
     */
    private BigDecimal payTotal;

}
