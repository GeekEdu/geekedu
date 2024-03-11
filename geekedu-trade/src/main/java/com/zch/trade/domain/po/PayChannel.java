package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@Data
@TableName("pay_channel")
public class PayChannel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 支付方式
     */
    private String name;

    /**
     * 支付渠道签名
     */
    private String sign;

    /**
     * 支付方式 图片
     */
    private String image;

}
