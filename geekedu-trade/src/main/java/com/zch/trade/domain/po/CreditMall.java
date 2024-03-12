package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import com.zch.trade.enums.GoodsTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("credit_mall")
public class CreditMall extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
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
     * 若不是实物 则需要商品id
     */
    private Integer vId;

    /**
     * 商品类型id
     */
    private GoodsTypeEnums typeId;

    private Boolean isShow;

    @TableLogic
    private Boolean isDelete;

}
