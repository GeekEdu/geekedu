package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.user.domain.po.SysPermission;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/12
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> getPermissionByRoleId(Integer roleId);

}
