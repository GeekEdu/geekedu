package com.zch.api.dto.book;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/24
 */
@Data
public class EBookArticleForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 文章名
     */
    private String title;

    /**
     * 编辑器 MARKDOWN RICHTEXT
     */
    private String editor;

    /**
     * 电子书id
     */
    private Integer bookId;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 付费原内容
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
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 是否可以试读
     */
    private Boolean isFreeRead;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 浏览次数
     */
    private Long readCount;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

}
