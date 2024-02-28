package com.zch.user.controller;

import com.zch.api.dto.user.ChangePwdForm;
import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.api.vo.user.UserSimpleVO;
import com.zch.common.mvc.result.Response;
import com.zch.user.domain.po.User;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

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

    /**
     * 后台返回学员列表
     * @return
     */
    @GetMapping("/member/list")
    public Response getMemberList() {
        return Response.success();
    }

}
