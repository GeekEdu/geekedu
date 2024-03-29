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
     * 问题id
     */
    private Integer questionId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long thumbCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 回答人
     */
    private Long userId;

    /**
     * 是否正确答案
     */
    private Boolean isCorrect;

    /**
     * 答案的图片链接，使用 , 隔开
     */
    private String images;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
