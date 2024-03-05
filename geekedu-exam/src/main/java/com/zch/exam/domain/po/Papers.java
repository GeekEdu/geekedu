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
 * @date 2024/2/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("papers")
public class Papers extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷名
     */
    private String title;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 总分
     */
    private Integer score;

    /**
     * 考试时间
     */
    private Integer examTime;

    /**
     * 及格分数
     */
    private Integer passScore;

    /**
     * 是否免费
     */
    private Boolean isFree;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 考试次数 0无限制
     */
    private Integer tryTime;

    /**
     * 是否跳过阅卷
     */
    private Boolean skipMark;

    /**
     * 是否限制参考人 true为只有指定人才能参考
     */
    private Boolean enableInvite;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 关联课程
     */
    private Integer requireCourse;

    /**
     * 考试规则
     */
    private String rule;

    /**
     * 试题数
     */
    private Long questionCount;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
