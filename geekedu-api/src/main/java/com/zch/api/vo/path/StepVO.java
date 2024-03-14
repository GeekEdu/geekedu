package com.zch.api.vo.path;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StepVO extends BaseVO {

    private Integer id;

    /**
     * 路径id
     */
    private Integer pathId;

    /**
     * 课程数
     */
    private Integer courseCount;

    /**
     * 步骤名
     */
    private String name;

    /**
     * 简介
     */
    private String intro;

    /**
     * 排序
     */
    private Integer sort;

}
