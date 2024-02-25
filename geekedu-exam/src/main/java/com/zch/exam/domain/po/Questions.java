package com.zch.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("questions")
public class Questions extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题目分类
     */
    private Integer categoryId;

    /**
     * 题目类型
     */
    private Integer types;

    /**
     * 等级
     */
    private Integer levels;

    /**
     * 题干内容
     */
    private String content;

    /**
     * 答案
     */
    private String answer;

    /**
     * 分数
     */
    private BigDecimal score;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    /**
     * 解析
     */
    private String analysis;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
