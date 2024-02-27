package com.zch.api.dto.exam;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/2/27
 */
@Data
public class QuestionForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 分类
     */
    private String category;

    /**
     * 类型
     */
    private String type;

    /**
     * 等级
     */
    private String level;

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
    private String scores;

    /**
     * 解析
     */
    private String analysis;

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;
    private String option7;
    private String option8;
    private String option9;
    private String option10;

}
