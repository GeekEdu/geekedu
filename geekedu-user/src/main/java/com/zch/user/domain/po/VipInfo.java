package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Data
@TableName("vip_info")
public class VipInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String intro;

    private Integer day;

    private BigDecimal price;

}
