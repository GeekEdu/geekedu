package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sliders")
public class Sliders extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String thumb;

    private Integer sort;

    private String url;

    private LocalDateTime deletedTime;

    private String platform;

}
