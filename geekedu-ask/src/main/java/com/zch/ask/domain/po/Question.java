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
 * @date 2024/2/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("question")
public class Question extends BaseEntity {

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
     * 分类 id
     */
    private Integer categoryId;

    /**
     * 查看次数
     */
    private Long viewCount;

    /**
     * 回答数
     */
    private Long answerCount;

    /**
     * 点赞次数
     */
    private Long thumbCount;

    /**
     * 提问人
     */
    private Long userId;

    /**
     * 悬赏积分
     */
    private Integer rewardScore;

    /**
     * 问题状态 0-未解决 1-已解决
     */
    private Boolean questionStatus;

    /**
     * 问题内容中存在的图片链接，使用 , 分隔
     */
    private String images;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
