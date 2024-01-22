package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.user.domain.po.SysPermission;
import com.zch.user.domain.po.SysRolePermission;
import com.zch.user.mapper.SysPermissionMapper;
import com.zch.user.mapper.SysRolePermissionMapper;
import com.zch.user.service.ISysPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final SysPermissionMapper sysPermissionMapper;

    private final SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public List<SysPermission> getPermissionByRoleId(Integer roleId) {
        if (ObjectUtils.isNull(roleId)) {
            throw new CommonException("请传入正确的角色Id！！！");
        }
        List<SysRolePermission> sysRolePermissions = sysRolePermissionMapper.selectList(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
                .select(SysRolePermission::getId, SysRolePermission::getRoleId, SysRolePermission::getPermissionId));
        List<Integer> ids = sysRolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
        List<SysPermission> sysPermissions = sysPermissionMapper.selectBatchIds(ids);
        return sysPermissions;
    }
}
