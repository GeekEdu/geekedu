package com.zch.book.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.zch.common.meilisearch.annotation.MsField;
import com.zch.common.meilisearch.annotation.MsIndex;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 图文
 * @author Poison02
 * @date 2024/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MsIndex(uid = "topic", primaryKey = "id")
public class ImageText extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    @MsField(openSort = true, openFilter = true, key = "id", description = "主键")
    private Integer id;

    /**
     * 标题
     */
    @MsField(openSort = true, openFilter = true, key = "title", description = "标题")
    private String title;

    /**
     * 封面链接
     */
    @MsField(openSort = true, openFilter = true, key = "coverLink", description = "封面链接")
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
    @MsField(openSort = true, openFilter = true, key = "originalContent", description = "付费原内容")
    private String originalContent;

    /**
     * 付费渲染内容
     */
    @MsField(openSort = true, openFilter = true, key = "renderContent", description = "付费渲染内容")
    private String renderContent;

    /**
     * 免费原内容
     */
    @MsField(openSort = true, openFilter = true, key = "freeContent", description = "免费原内容")
    private String freeContent;

    /**
     * 免费渲染内容
     */
    @MsField(openSort = true, openFilter = true, key = "freeRenderContent", description = "免费渲染内容")
    private String freeRenderContent;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 编辑器 目前有 MARKDOWN RICHTEXT
     */
    private String editor;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
