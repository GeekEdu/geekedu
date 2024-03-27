package com.zch.trade.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Poison02
 * @date 2024/3/27
 */
@Data
public class OrderCountDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private LocalDate orderDate;

    private Long count;

}
