package com.zch.book.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Data
@TableName("step_relation")
public class StepRelation {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 步骤id
     */
    private Integer stepId;

    /**
     * 关联商品id
     */
    private Integer relationId;

    /**
     * 关联类型
     */
    private Byte relationType; // tinyint usually maps to a Java byte, but check your DB specifics

    /**
     * 路径id
     */
    private Integer pathId;

    @TableLogic
    private Boolean isDelete; // tinyint(1) often represents a boolean, use Byte if it's a flag with multiple values

    /**
     * 商品封面
     */
    private String cover;

    /**
     * 商品价格
     */
    private BigDecimal price;

}
