package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("notice")
public class Notice extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private Long adminId;

    private String announcement;

    private Integer viewCount;

    @TableLogic
    private Boolean isDelete;

}
