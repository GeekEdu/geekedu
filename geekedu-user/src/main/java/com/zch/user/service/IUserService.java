package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.LoginVO;
import com.zch.api.vo.user.UserRoleVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.user.domain.po.User;


/**
 * @author Poison02
 * @date 2024/1/11
 */
public interface IUserService extends IService<User> {

    /**
     * 生成验证码
     * @return
     */
    CaptchaVO getCaptcha();

    /**
     * 登录验证
     * @param form
     * @return
     */
    LoginVO login(LoginForm form);

    boolean addUser(User user);

    /**
     * 获取用户所有权限
     * @return
     */
    UserRoleVO getUsers();

    /**
     * 通过 userId 查询用户
     * @param userId
     * @return
     */
    UserSimpleVO getUserById(String userId);

}
