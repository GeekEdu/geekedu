package com.zch.api.dto.trade;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@Data
public class CreateOrderForm implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 优惠码 / 优惠券id
     */
    private String promoCode;

    /**
     * 支付方式
     */
    private String payment;

}
