package com.zch.user.controller;

import com.zch.api.dto.user.*;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.user.*;
import com.zch.common.meilisearch.service.IndexService;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.common.redis.annotation.RateLimiter;
import com.zch.common.redis.enums.LimitType;
import com.zch.user.domain.po.User;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    private final IndexService indexService;

    @GetMapping("/test")
    public Map<String, Object> test() {
        return indexService.all();
    }

    @GetMapping("/sms/phone")
    public Response<String> getSmsCode(@RequestParam("phone") String phone) {
        return Response.success(userService.getPhoneCode(phone));
    }

    /**
     * 绑定手机号
     * @param form
     * @return
     */
    @PostMapping("/phone/bind")
    public Response<WxLoginVO> bingPhone(@RequestBody BindPhoneForm form) {
        return Response.success(userService.bindPhone(form));
    }

    /**
     * 获取用户的所有权限
     * @return
     */
    @GetMapping("/users")
    public Response getUsers() {
        return Response.success(userService.getUsers());
    }

    /**
     * 返回图片二维码
     * @return
     */
    @RateLimiter(time = 60, count = 5, limitType = LimitType.IP)
    @GetMapping("/captcha/image")
    public Response<CaptchaVO> getCaptcha() {
        return Response.success(userService.getCaptcha());
    }

    /**
     * 通过用户名密码登录
     * @param form
     * @return
     */
    @PostMapping("/login")
    public Response login(@RequestBody LoginForm form) {
        return Response.success(userService.login(form));
    }

    /**
     * 前台 手机号-密码登录
     * @param form
     * @return
     */
    @PostMapping("/v2/login/password")
    public Response<LoginVO> passwordLogin(@RequestBody PwdLoginForm form) {
        return Response.success(userService.passwordLogin(form));
    }

    /**
     * 前台 手机号-短信验证码登录
     * @param form
     * @return
     */
    @PostMapping("/v2/login/sms")
    public Response codeLogin(@RequestBody CodeLoginForm form) {
        return Response.success();
    }

    /**
     * 获取登录用户的明细
     * @return
     */
    @GetMapping("/v2/detail")
    public Response<UserVO> getUserDetail() {
        return Response.success(userService.getLoginUserDetail());
    }

    /**
     * 前台 修改用户头像
     * @param file
     * @return
     */
    @PostMapping("/v2/avatar/update")
    public Response updateUserAvatar(@RequestParam("file") MultipartFile file) {
        return Response.success(userService.updateUserAvatar(file));
    }

    /**
     * 获取用户订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/v2/order/list")
    public PageResult<OrderFullVO> getMemberOrderList(@RequestParam("pageNum") Integer pageNum,
                                                      @RequestParam("pageSize") Integer pageSize) {
        return userService.getOrderPage(pageNum, pageSize);
    }

    /**
     * 前台 签到
     * @return
     */
    @PostMapping("/v2/sign")
    public Response<Integer> signIn() {
        return Response.success(userService.signIn());
    }

    /**
     * 更新用户积分
     * @param userId
     * @param point
     * @return
     */
    @PostMapping("/v2/updatePoint")
    public Response<Void> updateUserPoint(@RequestParam("userId") Long userId, @RequestParam("point") Long point) {
        userService.updateUserPoint(userId, point);
        return Response.success();
    }

    /**
     * 前台 查看用户是否签到
     * @return
     */
    @GetMapping("/v2/isSign")
    public Response<Boolean> isSign() {
        return Response.success(userService.isSign());
    }

    /**
     * 登出
     * @return
     */
    @GetMapping("/logout")
    public Response<Boolean> logout() {
        return Response.success(userService.logout());
    }

    /**
     * 更改密码
     * @param form
     * @return
     */
    @PostMapping("/changePwd")
    public Response editPwd(@RequestBody ChangePwdForm form) {
        return Response.success(userService.changePwd(form));
    }

    /**
     * 新增用户
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response addUser(@RequestBody RegForm form) {
        return Response.judge(userService.addUser(form));
    }

    /**
     * 通过用户id返回用户信息
     * @param userId
     * @return
     */
    @GetMapping("/getUserById")
    public Response<UserSimpleVO> getUserById(@RequestParam("id") String userId) {
        return Response.success(userService.getUserById(userId));
    }

}
