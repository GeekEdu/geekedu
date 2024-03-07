package com.zch.trade.domain.po;

import lombok.Data;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@Data
public class AliSandboxPay {

    private String traceNo;

    private double totalAmount;

    private String subject;

    private String alipayTraceNo;

}
