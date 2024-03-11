package com.zch.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.*;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.user.*;
import com.zch.common.core.utils.*;
import com.zch.common.core.utils.encrypt.EncryptUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.utils.CommonServletUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.user.domain.po.SysPermission;
import com.zch.user.domain.po.SysRole;
import com.zch.user.domain.po.User;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    private final ITagService tagService;

    private final IVipInfoService vipInfoService;

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
            RedisUtils.setCacheObject(LOGIN_USER_TOKEN + token, userId, Duration.ofSeconds(LOGIN_USER_TOKEN_TTL));
            // 且将用户id写入到ThreadLocal中
            UserContext.set("userId", userId);
            LoginVO vo = new LoginVO();
            vo.setToken(token);
            return vo;
        }
        return new LoginVO();
    }

    @Override
    public LoginVO passwordLogin(PwdLoginForm form) {
        if (ObjectUtils.isNull(form)) {
            return null;
        }
        String mobile = form.getPhone();
        String password = form.getPassword();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, mobile));
        if (ObjectUtils.isNull(user)) {
            return null;
        }
        String salt = user.getSalt();
        String encrypt = EncryptUtils.md5Encrypt(password, salt);
        if (! encrypt.equals(user.getPassword())) {
            return null;
        }
        Long userId = user.getId();
        StpUtil.login(userId);
        String token = StpUtil.getTokenValue();
        RedisUtils.setCacheObject(LOGIN_USER_TOKEN + token, userId, Duration.ofSeconds(LOGIN_USER_TOKEN_TTL));
        // 且将用户id写入到ThreadLocal中
        UserContext.set("userId", userId);
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        return vo;
    }

    @Override
    public LoginVO codeLogin(CodeLoginForm form) {
        return null;
    }

    @Override
    public UserVO getLoginUserDetail() {
//        Long userId = UserContext.getLoginId();
//        if (ObjectUtils.isNull(userId)) {
//            return null;
//        }
//        User user = userMapper.selectById(userId);
        User user = userMapper.selectById(1745747394693820416L);
        if (ObjectUtils.isNull(user)) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public Integer signIn() {
        // 返回当前登录用户
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 拼接key 这里拼接后缀 使用 年月
        String suffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        // 和用户标识进行拼接
        String key = USER_SIGN_KEY + userId + suffix;
        // 获取今天是本月的第几天
        int day = now.getDayOfMonth();
        // 写入redis中 签到使用 BitMap 数据结构
        RedisUtils.setBitMap(key, day - 1);
        // 返回积分，这里设计一个积分规则，看今天是本月第几次签到，则给多少积分
        // TODO
        return 1;
    }

    @Override
    public Boolean isSign() {
        // 当前登录用户
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 拼接key 这里拼接后缀 使用 年月
        String suffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        // 和用户标识进行拼接
        String key = USER_SIGN_KEY + userId + suffix;
        // 获取今天是本月的第几天
        int day = now.getDayOfMonth();
        return RedisUtils.getBigMap(key, day);
    }

    @Override
    public Boolean changePwd(ChangePwdForm form) {
        if (ObjectUtils.isNull(form)) {
            return false;
        }
        Long userId = UserContext.getLoginId();
        User user = userMapper.selectById(userId);
        if (ObjectUtils.isNull(user)) {
            return false;
        }
        String salt = user.getSalt();
        // 得到原来的密码和数据库中相比
        String oldPwd = form.getOldPassword();
        String decrypt = EncryptUtils.md5Encrypt(oldPwd, salt);
        if (! user.getPassword().equals(decrypt)) {
            return false;
        }
        String newPwd = form.getNewPassword();
        String newPwdConfirm = form.getNewPasswordConfirm();
        if (StringUtils.isNotBlank(newPwd) && StringUtils.isNotBlank(newPwdConfirm) && ! newPwd.equals(newPwdConfirm)) {
            return false;
        }
        // 加密之后存储
        String encrypt = EncryptUtils.md5Encrypt(newPwdConfirm, salt);
        user.setPassword(encrypt);
        user.setUpdatedBy(userId);
        return updateById(user);
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

    @Override
    public UserSimpleVO getUserById(String userId) {
        // 获取请求 拿到该请求对应的信息
        HttpServletRequest request = CommonServletUtils.getRequest();
        Map<String, String> res = AddressUtils.getAddress(request);
        String ip = res.get("ip");
        String province = res.get("province");
        String browser = res.get("browser");
        String os = res.get("os");
        User user = userMapper.selectById(userId);
        UserSimpleVO vo = new UserSimpleVO();
        BeanUtils.copyProperties(user, vo);
        vo.setIpAddress(ip);
        vo.setProvince(province);
        vo.setBrowser(browser);
        vo.setOs(os);
        vo.setUserId(user.getId());
        return vo;
    }

    @Override
    public MemberFullVO getMemberPage(Integer pageNum, Integer pageSize, String sort, String order,
                                      String keywords, Integer vipId, Integer tagId, List<String> createdTime) {
        if (ObjectUtils.isNull(pageNum) || ObjectUtils.isNull(pageSize)
        || StringUtils.isBlank(sort) || StringUtils.isBlank(order)) {
            pageNum = 1;
            pageSize = 10;
            sort = "id";
            order = "desc";
        }
        MemberFullVO vo = new MemberFullVO();
        // 查找所有标签和vip信息
        List<TagVO> tagList = tagService.getTagList();
        List<VipVO> vipList = vipInfoService.getVipList();
        vo.setVip(vipList);
        vo.setTags(tagList);
        long count = count();
        if (count == 0) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(User::getName, keywords);
        }
        if (ObjectUtils.isNotNull(vipId)) {
            wrapper.eq(User::getVipId, vipId);
        }
        if (ObjectUtils.isNotNull(tagId)) {
            wrapper.eq(User::getTagId, tagId);
        }
        // 时间处理
        if (ObjectUtils.isNotNull(createdTime) && CollUtils.isNotEmpty(createdTime) && createdTime.size() > 1
            && StringUtils.isNotBlank(createdTime.get(0)) && StringUtils.isNotBlank(createdTime.get(0))) {
            List<LocalDateTime> times = timeHandle(createdTime);
            // 增加条件
            wrapper.between(User::getCreatedTime, times.get(0), times.get(1));
        }
        // 排序
        wrapper.orderBy(true, "asc".equals(order), User::getId);
        // 分页查找
        Page<User> page = page(new Page<User>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.getData().setTotal(0);
            vo.getData().setData(new ArrayList<>(0));
            return vo;
        }
        List<User> records = page.getRecords();
        List<UserVO> list = new ArrayList<>(records.size());
        for (User item : records) {
            UserVO vo1 = new UserVO();
            BeanUtils.copyProperties(item, vo1);
            // 查找相关的标签和vip
            String[] tagIds = item.getTagId().split(",");
            if (tagIds.length < 1) {
                vo1.setTag(new ArrayList<>(0));
            } else if (tagIds.length == 1 && "".equals(tagIds[0])) {
                vo1.setTag(new ArrayList<>(0));
            } else {
                List<TagVO> tags = new ArrayList<>(tagIds.length);
                for (String e : tagIds) {
                    TagVO tag = tagService.getTagById(Integer.valueOf(e));
                    tags.add(tag);
                }
                vo1.setTag(tags);
            }
            VipVO vip = vipInfoService.getVipById(item.getVipId());
            vo1.setVip(vip);
            list.add(vo1);
        }
        vo.getData().setTotal(count);
        vo.getData().setData(list);
        return vo;
    }

    @Override
    public VipAndTagVO getVipAndTagList() {
        List<VipVO> vips = vipInfoService.getVipList();
        List<TagVO> tags = tagService.getTagList();
        VipAndTagVO vo = new VipAndTagVO();
        vo.setTag(tags);
        vo.setVip(vips);
        return vo;
    }

    @Override
    public UserVO getUserById(Long id) {
        if (ObjectUtils.isNull(id)) {
            return new UserVO();
        }
        User user = userMapper.selectById(id);
        if (ObjectUtils.isNull(user)) {
            return new UserVO();
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        // 查找相关的标签和vip
        String[] tagIds = user.getTagId().split(",");
        if (tagIds.length < 1) {
            vo.setTag(new ArrayList<>(0));
        } else if (tagIds.length == 1 && "".equals(tagIds[0])) {
            vo.setTag(new ArrayList<>(0));
        } else {
            List<TagVO> tags = new ArrayList<>(tagIds.length);
            for (String e : tagIds) {
                TagVO tag = tagService.getTagById(Integer.valueOf(e));
                tags.add(tag);
            }
            vo.setTag(tags);
        }
        VipVO vip = vipInfoService.getVipById(user.getVipId());
        vo.setVip(vip);
        return vo;
    }

    @Override
    public Page<OrderVO> getMemberOrderList(Long id, Integer pageNum, Integer pageSize) {
        return new Page<>();
    }

    @Override
    public List<VipVO> getVipList() {
        return vipInfoService.getVipList();
    }

    @Override
    public VipVO getVipById(Integer id) {
        return vipInfoService.getVipById(id);
    }

    @Override
    public Boolean addVip(VipForm form) {
        return vipInfoService.addVip(form);
    }

    @Override
    public Boolean deleteVip(Integer id) {
        return vipInfoService.deleteVip(id);
    }

    @Override
    public Boolean updateVip(Integer id, VipForm form) {
        return vipInfoService.updateVip(id, form);
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

    /**
     * 对时间的处理
     * @param time
     * @return
     */
    public static List<LocalDateTime> timeHandle(List<String> time) {
        List<LocalDateTime> res = new ArrayList<>(2);
        String start = time.get(0);
        String end = time.get(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
        res.add(startDateTime);
        res.add(endDateTime);
        return res;
    }

    public static void main(String[] args) {
        String[] list = "1,2,3,4,5".split(",");
        System.out.println(Arrays.toString(list));
    }
}
