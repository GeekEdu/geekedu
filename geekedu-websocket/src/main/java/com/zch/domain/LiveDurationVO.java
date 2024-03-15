package com.zch.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@Data
public class LiveDurationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    private BigDecimal duration;

}
