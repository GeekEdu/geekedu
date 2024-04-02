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
 * @date 2024/2/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("e_book")
public class EBook extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电子书名字
     */
    private String name;

    /**
     * 简短介绍
     */
    private String shortDesc;

    /**
     * 详细介绍
     */
    private String fullDesc;

    /**
     * 封面链接
     */
    private String coverLink;

    /**
     * 阅读次数
     */
    private Long readCount;

    /**
     * 销售类型
     */
    private Boolean sellType;

    /**
     * 章节数
     */
    private Integer chapterCount;

    /**
     * 文章数
     */
    private Integer articleCount;

    /**
     * 销量
     */
    private Long sellCount;

    /**
     * 收藏数
     */
    private Long collectCount;

    /**
     * 学员数量
     */
    private Long userCount;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 分类id
     */
    private Integer categoryId;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
