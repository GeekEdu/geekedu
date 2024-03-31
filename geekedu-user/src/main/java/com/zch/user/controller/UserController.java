package com.zch.user.controller;

import com.zch.api.dto.user.ChangePwdForm;
import com.zch.api.dto.user.CodeLoginForm;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.dto.user.PwdLoginForm;
import com.zch.api.vo.trade.order.OrderFullVO;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.LoginVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.api.vo.user.UserVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.common.redis.annotation.RateLimiter;
import com.zch.common.redis.enums.LimitType;
import com.zch.user.domain.po.User;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/sms/phone")
    public Response<String> getSmsCode(@RequestParam("phone") String phone) {
        return Response.success(userService.getPhoneCode(phone));
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
    public Response logout() {
        return Response.success();
    }

    @PostMapping("/changePwd")
    public Response editPwd(@RequestBody ChangePwdForm form) {
        return Response.success(userService.changePwd(form));
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Response addUser(@RequestBody User user) {
        return Response.judge(userService.addUser(user));
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
