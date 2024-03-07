package com.zch.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 练习对应题库表
 * @author Poison02
 * @date 2024/3/7
 */
@Data
@TableName("p_question")
public class PQuestion {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 练习id
     */
    private Integer pId;

    /**
     * 题id
     */
    private Integer qId;

    /**
     * 章节id
     */
    private Integer cId;

    @TableLogic
    private Boolean isDelete;

}
