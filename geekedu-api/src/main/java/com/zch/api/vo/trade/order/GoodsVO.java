package com.zch.api.vo.trade.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsVO extends BaseVO {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 商品封面
     */
    private String goodsCover;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

}
