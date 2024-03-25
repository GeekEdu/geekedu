package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
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
@TableName("second_kill")
public class SecondKill extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableLogic
    private Boolean isDelete;

}
