package com.zch.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@Data
@TableName("danmu")
public class Danmu {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer courseId;

    private Integer videoId;

    private Long userId;

    private BigDecimal duration;

}
