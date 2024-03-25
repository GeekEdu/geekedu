package com.zch.api.vo.trade.seckill;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SecondKillVO extends BaseVO {

    private Integer id;

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

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 是否开始
     */
    private Boolean isStart;

    /**
     * 是否结束
     */
    private Boolean isOver;

}
