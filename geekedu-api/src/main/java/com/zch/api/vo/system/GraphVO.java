package com.zch.api.vo.system;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GraphVO extends BaseVO {

    /**
     * 每日注册用户
     */
    private Map<LocalDate, Long> userRegister = new HashMap<>(0);

    /**
     * 每日创建订单
     */
    private Map<LocalDate, Long> orderCreated = new HashMap<>(0);

    /**
     * 每日已支付订单
     */
    private Map<LocalDate, Long> orderPaid = new HashMap<>(0);

    /**
     * 每日收入
     */
    private Map<LocalDate, BigDecimal> orderSum = new HashMap<>(0);

}
