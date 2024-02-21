package com.zch.api.vo.book;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageTextVO extends BaseVO {

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面链接
     */
    private String coverLink;

    /**
     * 阅读次数
     */
    private Long readCount;

    /**
     * 销售类型 0-免费 1-收费
     */
    private Boolean sellType;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 点赞次数
     */
    private Long thumbCount;

    /**
     * 收藏次数
     */
    private Long collectCount;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否展示 0-否 1-是
     */
    private Boolean isShow;

    /**
     * 销量
     */
    private Long sellCount;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 购买学员数
     */
    private Long userCount;

    /**
     * 付费原内容 也是文章内容
     */
    private String originalContent;

    /**
     * 付费渲染内容
     */
    private String renderContent;

    /**
     * 免费原内容
     */
    private String freeContent;

    /**
     * 免费渲染内容
     */
    private String freeRenderContent;

    private CategorySimpleVO category;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 编辑器
     */
    private String editor;

    private Long createdBy;

    private LocalDateTime createdTime;

    private Long updatedBy;

    private LocalDateTime updatedTime;

    private Boolean isDelete;

}
