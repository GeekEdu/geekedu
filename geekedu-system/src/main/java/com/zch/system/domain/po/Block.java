package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("block")
public class Block extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String sign;

    /**
     * 子模块id，使用 , 分割
     */
    private String itemIds;

}
