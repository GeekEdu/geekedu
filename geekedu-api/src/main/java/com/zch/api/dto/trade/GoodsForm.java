package com.zch.api.dto.trade;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Data
public class GoodsForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String title;

    private String cover;

    private String intro;

    private Integer price;

    private Integer stockCount;

    private Boolean isShow;

    private Boolean isVirtual;

    private String goodsType;

    /**
     * 虚拟商品id
     */
    private Integer vId;

}
