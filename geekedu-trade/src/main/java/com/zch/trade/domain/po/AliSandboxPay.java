package com.zch.trade.domain.po;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@Data
public class AliSandboxPay {

    /**
     * 商家订单号
     */
    private String outTraceNo;

    private double totalAmount;

    private String subject;

    private String alipayTraceNo;

    private String returnUrl;

}
