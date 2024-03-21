package com.zch.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.user.*;
import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.feignClient.resources.MediaFeignClient;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.utils.AddressUtils;
import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.book.record.StudyRecordVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.resources.FileUploadVO;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.user.*;
import com.zch.common.core.utils.*;
import com.zch.common.core.utils.encrypt.EncryptUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.common.mvc.utils.CommonServletUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.satoken.context.UserContext;
import com.zch.user.domain.po.Collection;
import com.zch.user.domain.po.SysPermission;
import com.zch.user.domain.po.SysRole;
import com.zch.user.domain.po.User;
import com.zch.user.enums.CollectionEnums;
import com.zch.user.mapper.UserMapper;
import com.zch.user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    private final ApplicationContext applicationContext;

    private final MediaFeignClient mediaFeignClient;

    private final TradeFeignClient tradeFeignClient;

    private final ICollectionService collectionService;

    private final BookFeignClient bookFeignClient;

    private final CourseFeignClient courseFeignClient;

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
        // 查询会员信息
        if (ObjectUtils.isNotNull(user.getVipId())) {
            VipVO vip = vipInfoService.getVipById(user.getVipId());
            vo.setVip(vip);
        }
        return vo;
    }

    @Override
    public Boolean updateUserAvatar(MultipartFile file) {
        // 上传图片得到结果
        Response<FileUploadVO> response = mediaFeignClient.uploadFile(file, 0);
        if (ObjectUtils.isNotNull(response) && ObjectUtils.isNotNull(response.getData())) {
            User user = new User();
            // Long userId = UserContext.getLoginId();
            Long userId = 1745747394693820416L;
            user.setId(userId);
            user.setAvatar(response.getData().getLink());
            updateById(user);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<OrderFullVO> getOrderPage(Integer pageNum, Integer pageSize) {
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        return tradeFeignClient.getOrderPage(userId, pageNum, pageSize);
    }

    @Override
    public Integer signIn() {
        // 返回当前登录用户
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        Map<String, Object> res = spliceKey(userId);
        String key = res.get("key").toString();
        int day = (int) res.get("day");
        // 增加MQ 写入数据库测试 后续重构 TODO
//        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("save2MysqlProducer");
//        User user = userMapper.selectById(userId);
//        JSONObject json = new JSONObject();
//        Message msg = new Message();
//        msg.setTopic(MQConstants.SAVE_TO_MYSQL_TOPIC);
        // 这里判断的前提是：今天是本月第一天，查看今天是否签到，如果已经签了 则返回对应积分，若没有，则进行签到，再返回对应积分
        if (day == 1 && isSign()) {
//            user.setPoints(user.getPoints() + 1);
//            json.put("message", user);
//            msg.setBody(json.toJSONString().getBytes(StandardCharsets.UTF_8));
//            RocketMQUtils.asyncSendMsg(producer, msg);
            return 1;
        }
        // 写入redis中 签到使用 BitMap 数据结构
        RedisUtils.setBitMap(key, day - 1);
        // 返回积分，这里设计一个积分规则，看今天是本月第几次签到，则给多少积分
        /**
         * 积分规则：
         *   - 未有连续签到
         *     用户截止目前，本月签到的天数，签到天数即为得到的积分数
         *   - 有连续签到
         *     先得到本月签到的天数，再找到本月连续签到的地方（可能不止一处）
         *     连续签到的积分计算：从第一次签到开始，往后，每连续一次，则当天积分+1；如第一天1，第二天2，第三天3...
         *     最后将所得积分入库，返回前端展示
         *
         */
        // 1. 获取用户本月签到天数
        long signDays = RedisUtils.getBitCount(key);
        if (signDays < 2) {
//            user.setPoints(user.getPoints() + 1);
//            json.put("message", user);
//            msg.setBody(json.toJSONString().getBytes(StandardCharsets.UTF_8));
//            RocketMQUtils.asyncSendMsg(producer, msg);
            return 1;
        }
        // 2. 获取截止当前日期，本月签到情况整数值。后续转为二进制进行比较
        long signed = RedisUtils.getUnsigned(key, day);
        // 签到情况可能是：101010101  11001011011 分为两类：连续或者不连续
        String binarySign = Long.toBinaryString(signed);
        // 计算最大连续签到天数
        int longSignDays = longSignDays(binarySign);
        if (longSignDays < 2) {
            // 如果最大连续签到天数已经小于2天 则直接计算签到天数的积分即可
            // 这种情况则是没有连续签到的情况
            return Math.toIntExact(signDays);
        } {
            // 存在连续签到的情况
            // 将所有签到情况汇总
            Map<Integer, Integer> signRes = collectSign(binarySign);
            // 累加所有类型的积分即可！
            int credit = 0;
            for (Integer e : signRes.values()) {
                credit += e;
            }
//            user.setPoints(user.getPoints() + credit);
//            json.put("message", user);
//            msg.setBody(json.toJSONString().getBytes(StandardCharsets.UTF_8));
//            RocketMQUtils.asyncSendMsg(producer, msg);
            return credit;
        }
    }

    @Override
    public Boolean isSign() {
        // 当前登录用户
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        Map<String, Object> res = spliceKey(userId);
        return RedisUtils.getBigMap(res.get("key").toString(),  ((int) res.get("day")) - 1);
    }

    /**
     * 拼接用户签到 Key
     * @param userId
     * @return
     */
    private Map<String, Object> spliceKey(Long userId) {
        Map<String, Object> res = new HashMap<>(2);
        // 获取日期
        LocalDateTime now = LocalDateTime.now();
        // 拼接key 这里拼接后缀 使用 年月
        String suffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        // 和用户标识进行拼接
        String key = USER_SIGN_KEY + userId + suffix;
        // 获取今天是本月的第几天
        int day = now.getDayOfMonth();
        res.put("day", day);
        res.put("key", key);
        return res;
    }

    /**
     * 计算最大连续签到天数
     * @param binarySign
     * @return
     */
    private int longSignDays(String binarySign) {
        int maxLen = 0;
        int current = 0;
        for (char item : binarySign.toCharArray()) {
            if ((item & 1) == 1) {
                // 如果当前天已签到
                current++;
                // 更新最大连续签到天数
                maxLen = Math.max(current, maxLen);
            } else {
                // 则重置签到天数
                current = 0;
            }
        }
        return maxLen;
    }

    /**
     * 汇总全部签到情况
     * @param binarySign
     * @return
     */
    private Map<Integer, Integer> collectSign(String binarySign) {
        Map<Integer, Integer> res = new HashMap<>();
        // 计数器
        int count = 0;
        // 签到天数
        int current = 0;
        // 连续签到天数
        int continuous = 0;
        for (char item : binarySign.toCharArray()) {
            if ((item & 1) == 1) {
                current++;
                continuous = current;
            } else {
                // 这里没有考虑这种情况：101100111，即最后是1
                current = 0;
                // 先将连续签到的情况放入map中
                res.put(count++, continuous == 0 ? 0 : (calCredit(continuous)));
                // 将连续签到天数归0
                continuous = 0;
            }
        }
        // 加上最后一天是1的情况 最后一天一定是1
        if (continuous == 0) {
            // 连续天数为0，即最后一天的前一天未签到，则积分为1
            res.put(count, 1);
        } else {
            // 连续天数不为0，即至少最后一天的前一天签到了的，计算积分
            res.put(count, calCredit(continuous));
        }
        return res;
    }

    /**
     * 根据天数 计算积分
     * @param day
     * @return
     */
    private int calCredit(int day) {
        int count = 0;
        for (int i = 1; i <= day; i++) {
            count += i;
        }
        return count;
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

    @Override
    public List<UserSimpleVO> getTeacherList() {
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getRoleId, 2));
        if (ObjectUtils.isNull(users) || CollUtils.isEmpty(users)) {
            return new ArrayList<>(0);
        }
        return users.stream().map(item -> {
            UserSimpleVO vo = new UserSimpleVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean queryIsVip(Long id) {
        User user = getById(id);
        if (ObjectUtils.isNull(user)) {
            return false;
        }
        return user.getVipId() != 0;
    }

    @Override
    public List<StudyRecordVO> queryCollectList(String type) {
        // 用户id
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 查询收藏表
        List<Collection> list = collectionService.queryList(userId, type);
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<StudyRecordVO> vo = new ArrayList<>();
        list.forEach(item -> {
            StudyRecordVO vo1 = new StudyRecordVO();
            vo1.setCollectTime(item.getUpdatedTime());
            vo1.setUserId(userId);
            vo1.setId(item.getId());
            if (CollectionEnums.IMAGE_TEXT.equals(CollectionEnums.valueOf(type))) {
                Response<ImageTextVO> res = bookFeignClient.getImageTextById(item.getRelationId());
                if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                    vo1.setTopicId(item.getRelationId());
                    vo1.setTopic(res.getData());
                }
            } else if (CollectionEnums.E_BOOK.equals(CollectionEnums.valueOf(type))) {
                Response<EBookVO> res = bookFeignClient.getEBookById(item.getRelationId());
                if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                    vo1.setBookId(item.getRelationId());
                    vo1.setBook(res.getData());
                }
            } else if (CollectionEnums.LIVE_COURSE.equals(CollectionEnums.valueOf(type))) {
                Response<LiveCourseVO> res = courseFeignClient.getLiveCourseDetail(item.getRelationId());
                if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                    vo1.setLiveCourseId(item.getRelationId());
                    vo1.setLiveCourse(res.getData());
                }
            } else if (CollectionEnums.REPLAY_COURSE.equals(CollectionEnums.valueOf(type))) {
                Response<CourseVO> res = courseFeignClient.getCourseById(item.getRelationId());
                if (ObjectUtils.isNotNull(res) && ObjectUtils.isNotNull(res.getData())) {
                    vo1.setCourseId(item.getRelationId());
                    vo1.setCourse(res.getData());
                }
            }
            vo.add(vo1);
        });
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
        long test = 123L;
        System.out.println(Long.toBinaryString(test));
        long test2 = 21341242L;
        System.out.println(Math.toIntExact(test2));
    }
}
