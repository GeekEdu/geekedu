package com.zch.api.vo.trade.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderGraphVO extends BaseVO {

    /**
     * 支付金额数
     */
    private Map<LocalDate, BigDecimal> orderPayAmount = new HashMap<>(0);

    /**
     * 支付订单数
     */
    private Map<LocalDate, Long> orderPayCount = new HashMap<>(0);

    /**
     * 支付人数
     */
    private Map<LocalDate, Long> orderPayNum = new HashMap<>(0);

    /**
     * 支付均价
     */
    private Map<LocalDate, BigDecimal> orderPayAvg = new HashMap<>(0);

}
