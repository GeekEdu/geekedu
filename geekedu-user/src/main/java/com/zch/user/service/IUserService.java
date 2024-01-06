package com.zch.user.service;

import com.zch.api.dto.user.LoginFormDTO;
import com.zch.common.domain.dto.LoginUserDTO;
import com.zch.user.domain.po.User;

/**
 * @author Poison02
 * @date 2024/1/6
 */
public interface IUserService {

    /**
     * 登录
     * @param loginFormDTO 登录表单实体
     * @param isAdmin 是否管理员 0-是 1-否
     * @return
     */
    LoginUserDTO login(LoginFormDTO loginFormDTO, boolean isAdmin);

    int insertUser(User user);

}
