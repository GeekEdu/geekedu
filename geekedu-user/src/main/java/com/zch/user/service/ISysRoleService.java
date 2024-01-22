package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.user.domain.po.SysRole;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/12
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> getRoleByUserId(Long userId);

}
