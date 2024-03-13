package com.zch.book.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Data
@TableName("steps")
public class Steps {

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableLogic
    private Boolean isDelete;

}
