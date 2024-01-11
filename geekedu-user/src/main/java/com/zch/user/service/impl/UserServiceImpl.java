package com.zch.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.utils.CaptchaUtils;
import com.zch.common.utils.redis.RedisUtils;
import com.zch.user.domain.form.LoginForm;
import com.zch.user.domain.po.User;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public Map<String, Object> getCaptcha() {
        Map<String, String> captcha = CaptchaUtils.createPicCaptcha();
        HashMap<String, Object> result = new HashMap<>(captcha);
        result.put("sensitive", false);
        return result;
    }

    @Override
    public Map<String, Object> login(LoginForm form) {
        RedisUtils.setCacheObject("username", form.getUsername());
        return null;
    }
}
