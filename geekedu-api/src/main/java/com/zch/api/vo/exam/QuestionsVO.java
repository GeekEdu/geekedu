package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionsVO extends BaseVO {

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

    /**
     * 分类
     */
    private TagsVO category;

}
