package com.zch.api.dto.path;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @author Poison02
 * @date 2024/3/14
 */
@Data
public class LearnPathForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String cover;

    private Integer categoryId;

    private String price;

    private String originPrice;

    private LocalDateTime groundingTime;

    private Boolean isShow;

    private String intro;

}
