package com.zch.api.dto.trade.seckill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/26
 */
@Data
public class SecKillForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀价
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originPrice;

    /**
     * 剩余库存
     */
    private Integer stock;

    /**
     * 总量
     */
    private Integer total;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名
     */
    private String goodsTitle;

    /**
     * 商品封面
     */
    private String goodsCover;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 商品类型文本
     */
    private String goodsTypeText;

    /**
     * 秒杀开始时间
     */
    private LocalDateTime startAt;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endAt;

}
