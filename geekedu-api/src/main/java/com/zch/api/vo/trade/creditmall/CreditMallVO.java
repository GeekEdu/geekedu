package com.zch.api.vo.trade.creditmall;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreditMallVO extends BaseVO {

    private Integer id;

    private String title;

    private String cover;

    /**
     * 商品所需积分
     */
    private Integer price;

    /**
     * 库存
     */
    private Integer stockCount;

    /**
     * 兑换数
     */
    private Integer sellCount;

    /**
     * 商品介绍
     */
    private String intro;

    /**
     * 是否虚拟物品 默认是 true
     */
    private Boolean isVirtual;

    /**
     * 商品分类
     */
    private String goodsType;

    /**
     * 若不是实物 则需要商品id
     */
    private Integer vId;

    /**
     * 商品类型id
     */
    private Integer typeId;

    private Boolean isShow;

    private LocalDateTime createdTime;

}
