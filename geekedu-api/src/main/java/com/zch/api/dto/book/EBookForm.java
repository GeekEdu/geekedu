package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/24
 */
@Data
public class EBookForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 电子书名
     */
    private String name;

    /**
     * 封面链接
     */
    private String coverLink;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 是否免费
     */
    private Boolean sellType;

    /**
     * 价格
     */
    private String price;

    /**
     * 是否vip免费
     */
    // private Boolean isVipFree;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 简短描述
     */
    private String shortDesc;

    /**
     * 详细描述
     */
    private String fullDesc;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

}
