package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.user.domain.po.SysRole;
import com.zch.user.domain.po.SysUserRole;
import com.zch.user.mapper.SysRoleMapper;
import com.zch.user.mapper.SysUserRoleMapper;
import com.zch.user.service.ISysRoleService;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        if (ObjectUtils.isNull(userId)) {
            throw new CommonException("请传入正确的用户Id！！！");
        }
        // 通过userId查出相对应的角色
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
                .select(SysUserRole::getId, SysUserRole::getUserId, SysUserRole::getRoleId));
        // 通过查出来的roleId查询对应的角色信息
        List<Integer> ids = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        List<SysRole> sysRoles = sysRoleMapper.selectBatchIds(ids);
        return sysRoles;
    }

}
