package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/21
 */
@Data
public class ImageTextForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面链接
     */
    private String coverLink;

    /**
     * 销售类型
     */
    private Boolean isFree;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 价格
     */
    private String price;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 分类id
     */
    private Integer categoryId;

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

    /**
     * 编辑器 目前有 MARKDOWN RICHTEXT
     */
    private String editor;

}
