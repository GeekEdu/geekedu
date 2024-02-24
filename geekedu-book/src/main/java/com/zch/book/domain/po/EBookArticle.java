package com.zch.book.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("e_book_article")
public class EBookArticle extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
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

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
