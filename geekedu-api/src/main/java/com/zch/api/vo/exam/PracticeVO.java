package com.zch.api.vo.exam;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PracticeVO extends BaseVO {

    private Integer id;

    private String name;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 是否免费
     */
    private Boolean isFree;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 题目数
     */
    private Long questionCount;

    /**
     * 学员数
     */
    private Long userCount;

    private CTagsVO category;

    private LocalDateTime createdTime;

}
