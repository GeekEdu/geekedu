package com.zch.user.controller;

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

    @GetMapping("/users")
    public Response getUsers() {
        return Response.success(userService.getUsers());
    }

    @GetMapping("/captcha/image")
    public Response<CaptchaVO> getCaptcha() {
        return Response.success(userService.getCaptcha());
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginForm form) {
        return Response.success(userService.login(form));
    }

    @GetMapping("/logout")
    public Response logout() {
        return Response.success();
    }

    @PostMapping("/add")
    public Response addUser(@RequestBody User user) {
        return Response.judge(userService.addUser(user));
    }

    @GetMapping("/getUserById")
    public Response<UserSimpleVO> getUserById(@RequestParam("id") Long userId) {
        return Response.success(userService.getUserById(userId));
    }

}
