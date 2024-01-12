package com.zch.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.user.domain.po.SysPermission;
import com.zch.user.domain.po.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}
