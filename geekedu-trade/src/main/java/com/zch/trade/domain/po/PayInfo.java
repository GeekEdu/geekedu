package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.trade.enums.PayTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录信息表
 * @author Poison02
 * @date 2024/3/9
 */
@EqualsAndHashCode(callSuper = true)
@TableName("pay_info")
@Data
public class PayInfo extends BaseEntity {

    /**
     * 支付记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付名称 对应渠道名
     */
    private String payName;

    /**
     * 支付渠道
     */
    private PayTypeEnum payChannel;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付订单号
     */
    private String orderId;

    /**
     * 交易订单号
     */
    private String tradeNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 是否支付
     */
    private Boolean isPaid;

    @TableLogic
    private Boolean isDelete;

}
