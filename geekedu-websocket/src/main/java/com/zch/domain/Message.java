package com.zch.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer courseId;

    private Integer videoId;

    private String content;

    private BigDecimal duration;

}
