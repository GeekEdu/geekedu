package com.zch.book.domain.po;

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
 * @date 2024/3/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("learn_path")
public class LearnPath extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 路径名
     */
    private String name;

    /**
     * 现价
     */
    private BigDecimal price;

    /**
     * 封面
     */
    private String cover;

    /**
     * 原价 所有商品的总价
     */
    private BigDecimal originPrice;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 步骤数
     */
    private Integer stepCount;

    /**
     * 课程数
     */
    private Integer courseCount;

    /**
     * 简介
     */
    private String intro;

    @TableLogic
    private Boolean isDelete;

}
