package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MockVO extends BaseVO {

    private Integer id;

    /**
     * 考试名
     */
    private String title;

    /**
     * 分类id
     */
    private Integer categoryId;

    private Boolean isFree;

    private Boolean isVipFree;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 是否指定人
     */
    private Boolean isInvite;

    /**
     * 考试时间
     */
    private Integer examTime;

    /**
     * 总分
     */
    private Integer score;

    /**
     * 及格
     */
    private Integer passScore;

    /**
     * 模拟考试规则
     */
    private String rule;

    /**
     * 试题数
     */
    private Long questionCount;

    private CTagsVO category;

}
