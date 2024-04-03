package com.zch.api.vo.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单返回实体类
 * @author Poison02
 * @date 2024/2/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVO extends BaseVO {

    /**
     * 对外展示的是订单id 数据库中是 order_number
     */
    private String orderId = "202403151330123456";

    /**
     * 订单状态
     */
    private String orderStatusText = "未支付";

    /**
     * 订单备注
     */
    private String orderNotes;

    /**
     * 商品id
     */
    private Integer goodsId;

    private Integer goodsCount = 0;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品原价
     */
    private BigDecimal goodsPrice;

    /**
     * 优惠价格
     */
    private BigDecimal goodsDiscount;

    /**
     * 最终价格
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最大支付时间，超过这个时间，订单自动取消
     */
    private LocalDateTime cancelTime;

    /**
     * 订单创建时间
     */
    private LocalDateTime createdTime;

}
