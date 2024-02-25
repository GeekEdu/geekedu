package com.zch.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Data
@TableName("types")
public class Types {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

}
