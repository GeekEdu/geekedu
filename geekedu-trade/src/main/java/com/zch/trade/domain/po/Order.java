package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.trade.enums.OrderStatusEnum;
import com.zch.trade.enums.PayStatusEnum;
import com.zch.trade.enums.PayTypeEnum;
import com.zch.trade.enums.ProductTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 * @author Poison02
 * @date 2024/3/9
 */
@EqualsAndHashCode(callSuper = true)
@TableName("t_order")
@Data
public class Order extends BaseEntity {

    /**
     * 订单id
     */
    @TableId
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单备注
     */
    private String orderNotes;

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品类型
     */
    private ProductTypeEnum goodsType;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最终支付金额
     */
    private BigDecimal amount;

    /**
     * 是否使用优惠券
     */
    private Boolean isUseCoupon;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 支付类型
     */
    private PayTypeEnum payType;

    /**
     * 支付状态
     */
    private PayStatusEnum payStatus;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 取消时间 主要用于订单状态为1和2时，超时的最大时间，超时则更新订单状态
     */
    private LocalDateTime cancelTime;

    /**
     * 是否秒杀
     */
    private Boolean isSeckill;

    @TableLogic
    private Boolean isDelete;

}
