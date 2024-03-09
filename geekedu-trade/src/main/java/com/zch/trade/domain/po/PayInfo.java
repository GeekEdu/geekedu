package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.trade.enums.PayTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录信息表
 * @author Poison02
 * @date 2024/3/9
 */
@TableName("pay_info")
@Data
public class PayInfo {

    /**
     * 支付记录id
     */
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
     * 用户id
     */
    private Long userId;

    /**
     * 支付时间
     */
    private LocalDateTime createdTime;

    @TableLogic
    private Boolean isDelete;

}
