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

    private Integer roleId;

    private String promoCode;

    private String orderId;

}
