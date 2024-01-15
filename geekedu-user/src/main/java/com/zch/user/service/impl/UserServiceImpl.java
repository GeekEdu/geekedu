package com.zch.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.LoginVO;
import com.zch.common.exceptions.CommonException;
import com.zch.common.utils.*;
import com.zch.common.utils.encrypt.EncryptUtils;
import com.zch.common.utils.redis.RedisUtils;
import com.zch.user.domain.po.User;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.zch.common.constants.ErrorInfo.Msg.EXPIRE_CAPTCHA_CODE;
import static com.zch.common.constants.ErrorInfo.Msg.INVALID_VERIFY_CODE;
import static com.zch.common.constants.RedisConstants.*;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    @Override
    public CaptchaVO getCaptcha() {
        Map<String, String> captcha = CaptchaUtils.createPicCaptcha();
        String img = captcha.get("img");
        String key = captcha.get("key");
        // 存入 redis 中，过期时间为 60 秒
        RedisUtils.setCacheMap(CAPTCHA_MAP, captcha);
        RedisUtils.expire(CAPTCHA_MAP, CAPTCHA_KEY_TTL);
        CaptchaVO vo = new CaptchaVO();
        vo.setImg(img);
        vo.setKey(key);
        return vo;
    }

    @Override
    public LoginVO login(LoginForm form) {
        String username = form.getUsername();
        String password = form.getPassword();
        String phone = form.getPhone();
        String imageCaptcha = form.getImageCaptcha();
        String imageKey = form.getImageKey();
        // 从redis中取出来验证码相关
        Map<String, String> cacheObject = RedisUtils.getCacheMap(CAPTCHA_MAP);
        if (ObjectUtils.isEmpty(cacheObject)) {
            throw new CommonException(EXPIRE_CAPTCHA_CODE);
        }
        boolean result = false;
        if (StringUtils.isNotBlank(phone)) {
            result = handlePhoneLogin(phone, password);
        }
        // 校验 验证码是否相同
        boolean checkCaptcha = checkCaptcha(imageCaptcha, imageKey, cacheObject);
        if (! checkCaptcha) {
            throw new CommonException(INVALID_VERIFY_CODE);
        }
        // 进行登录
        Long userId = handlePasswordLogin(username, password);
        if (userId > 0 && checkCaptcha) {
            // 登录成功，将token写入
            StpUtil.login(userId);
            LoginVO vo = new LoginVO();
            vo.setToken(StpUtil.getTokenValue());
            return vo;
        }
        return new LoginVO();
    }

    @Override
    public boolean addUser(User user) {
        // 生成每个用户的 盐值
        String key = EncryptUtils.generateKey();
        user.setSalt(key);
        user.setPassword(EncryptUtils.md5Encrypt(user.getPassword(), key));
        user.setId(IdUtils.getId());
        userMapper.insert(user);
        return true;
    }

    /**
     * 校验验证码是否相同
     * @param imageCaptcha
     * @param imageKey
     * @param cacheObject
     * @return
     */
    private boolean checkCaptcha(String imageCaptcha, String imageKey, Map<String, String> cacheObject) {
        String redisCaptcha = cacheObject.get("code");
        String redisKey = cacheObject.get("key");
        if (StringUtils.isSameStringByUpperToLower(imageCaptcha, redisCaptcha)
            && Objects.equals(imageKey, redisKey)) {
            return true;
        }
        return false;
    }

    /**
     * 处理 手机号登录
     * @param phone
     * @param code
     * @return
     */
    private boolean handlePhoneLogin(String phone, String code) {
        return false;
    }

    /**
     * 处理 账号密码登录
     * @param username
     * @param password
     * @return
     */
    private Long handlePasswordLogin(String username, String password) {
        // 从数据库中使用username查找对应用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(StringUtils.isNotBlank(username), User::getUserName, username)
                .eq(User::getIsDelete, 0)
                .select(User::getId, User::getUserName, User::getPassword, User::getSalt));
        if (ObjectUtils.isNull(user)) {
            return -1L;
        }
        // 使用盐值将输入的密码加密和从数据库中查出来的密码进行对比
        String encrypt = EncryptUtils.md5Encrypt(password, user.getSalt());
        if (! encrypt.equals(user.getPassword())) {
            return -1L;
        }
        return user.getId();
    }
}
