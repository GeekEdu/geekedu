package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@Data
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Integer roleId;

}
