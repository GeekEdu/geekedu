package com.zch.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.*;
import com.zch.common.core.utils.CaptchaUtils;
import com.zch.common.core.utils.IdUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.core.utils.encrypt.EncryptUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.user.domain.po.SysPermission;
import com.zch.user.domain.po.SysRole;
import com.zch.user.domain.po.User;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.ISysPermissionService;
import com.zch.user.service.ISysRoleService;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.zch.common.core.constants.ErrorInfo.Msg.EXPIRE_CAPTCHA_CODE;
import static com.zch.common.core.constants.ErrorInfo.Msg.INVALID_VERIFY_CODE;
import static com.zch.common.redis.constants.RedisConstants.*;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    private final ISysRoleService sysRoleService;

    private final ISysPermissionService sysPermissionService;

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
            String token = StpUtil.getTokenValue();
            // 且将用户id写入到ThreadLocal中
            UserContext.set("userId", userId);
            LoginVO vo = new LoginVO();
            vo.setToken(token);
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

    @Override
    public UserRoleVO getUsers() {
        // 获取用户Id
        Object userId = StpUtil.getLoginId();
        // 从redis中查找对应用户的信息
        UserRoleVO vo = RedisUtils.getCacheObject(USERINFO + userId);
        return vo;
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
        // 查找当前用户的权限，存入redis中
        handleRP2Redis(user);
        return user.getId();
    }

    /**
     * 处理角色和权限 存入redis中
     * @param user
     */
    private void handleRP2Redis(User user) {
        // 角色信息
        List<SysRole> roleByUserId = sysRoleService.getRoleByUserId(user.getId());
        List<Integer> permissionIds = new ArrayList<>();
        List<RoleVO> roles = new ArrayList<>();
        Map<String, Integer> permissionMap = new ConcurrentHashMap<>();
        RoleVO role = new RoleVO();
        List<Integer> roleId = new ArrayList<>();
        for (SysRole item : roleByUserId) {
            List<SysPermission> permissions = sysPermissionService.getPermissionByRoleId(item.getId());
            for (SysPermission e : permissions) {
                permissionMap.put(e.getSlug(), e.getId());
                permissionIds.add(e.getId());
            }
            roleId.add(item.getId());
            role.setId(item.getId());
            role.setDescription(item.getDescription());
            role.setDisPlayName(item.getDisplayName());
            role.setSlug(item.getSlug());
            role.setCreatedTime(item.getCreatedTime());
            role.setUpdatedTime(item.getUpdatedTime());
            role.setPermissionIds(permissionIds);
            PivotVO pivotVO = new PivotVO();
            pivotVO.setAdministratorId(1745747394693820416L);
            pivotVO.setRoleId(item.getId());
            role.setPivot(pivotVO);
            roles.add(role);
        }
        if (! RedisUtils.hasKey(ROLE_LIST + user.getId())) {
            // 将角色存入redis中
            RedisUtils.setCacheObject(ROLE_LIST + user.getId(), roles);
        }
        if (! RedisUtils.hasKey(PERMISSION_MAP + user.getId())) {
            // 将权限存入redis中
            RedisUtils.setCacheMap(PERMISSION_MAP + user.getId(), permissionMap);
        }
        if (! RedisUtils.hasKey(USERINFO + user.getId())) {
            // 组装用户信息 存入redis
            UserRoleVO vo = new UserRoleVO();
            vo.setCreatedTime(user.getCreatedTime());
            vo.setUpdatedTime(user.getUpdatedTime());
            vo.setEmail(user.getEmail());
            vo.setId(user.getId());
            vo.setLastLoginDate(LocalDateTime.now());
            vo.setLastLoginIp("127.0.0.1");
            vo.setLoginCount(111);
            vo.setName(user.getUserName());
            vo.setPermissions(permissionMap);
            vo.setRoles(roles);
            vo.setRoleId(roleId);
            RedisUtils.setCacheObject(USERINFO + user.getId(), vo);
        }
    }
}
