package com.zch.ask.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("answer")
public class Answer extends BaseEntity {

    /**
     * 问题id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 浏览数
     */
    private Long viewCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 提问人
     */
    private Long userId;

    /**
     * 悬赏积分
     */
    private Integer rewardScore;

    /**
     * 问题类型 0-未解决，1-已解决
     */
    private Boolean questionStatus;

    /**
     * 问题内容中的图片链接，使用 . 隔开
     */
    private String images;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
