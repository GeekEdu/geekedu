package com.zch.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.user.domain.form.LoginForm;
import com.zch.user.domain.po.User;

import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/11
 */
public interface IUserService extends IService<User> {

    /**
     * 生成验证码
     * @return
     */
    Map<String, Object> getCaptcha();

    Map<String, Object> login(LoginForm form);

}
