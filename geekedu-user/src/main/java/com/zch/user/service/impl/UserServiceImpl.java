package com.zch.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zch.common.domain.dto.LoginUserDTO;
import com.zch.common.exceptions.BadRequestException;
import com.zch.common.exceptions.ForbiddenException;
import com.zch.common.utils.IdUtils;
import com.zch.user.domain.po.User;
import com.zch.user.enums.UserStatus;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.zch.common.enums.UserType.*;
import static com.zch.user.constants.UserConstants.*;
import static com.zch.user.constants.UserErrorInfo.Msg.*;
import static com.zch.user.enums.UserStatus.*;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Override
    public LoginUserDTO login() {
        return null;
    }

    @Override
    public int insertUser(User user) {
        user.setId(IdUtils.getId());
        user.setType((short) ADMIN.getValue());
        user.setStatus((short) NORMAL.getValue());
        user.setCreatedBy(user.getUserName());
        user.setCreatedTime(new Date());
        user.setUpdatedBy(user.getUserName());
        user.setUpdatedTime(new Date());
        user.setIsDelete((short) 0);
        return userMapper.insert(user);
    }

    /**
     * 通过密码登录
     * @param loginFormDTO
     * @return
     */
    /*private User loginByPassword(LoginFormDTO loginFormDTO) {
        // 1. 校验用户名和手机号
        String userName = loginFormDTO.getUserName();
        String phone = loginFormDTO.getPhone();
        if (StrUtil.isBlank(userName) && StrUtil.isBlank(phone)) {
            throw new BadRequestException(INVALID_UN);
        }
        // 2. 根据用户名或手机号查询
        User user = userMapper.selectByUserNameOrPhoneUser(userName, phone);
        // 3. 校验是否被禁用
        if (user.getStatus() == UserStatus.FROZEN.getValue()) {
            throw new ForbiddenException(USER_FROZEN);
        }
        // 4. 校验密码 TODO
        return user;
    }*/

    /**
     * 通过手机号登录
     * @param phone
     * @param code
     * @return
     */
    private User loginByPhone(String phone, String code) {
        return null;
    }

    /**
     * 处理角色ID
     * @param user
     * @return
     */
    private Long handleRoleId(User user) {
        Long roleId = 0L;
        switch (user.getType()) {
            case 0:
                roleId = ADMIN_ROLE_ID;
                break;
            case 1:
                roleId = TEACHER_ROLE_ID;
                break;
            case 2:
                roleId = STUDENT_ROLE_ID;
                break;
        }
        return roleId;
    }
}
