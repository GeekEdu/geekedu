package com.zch.user.controller;

import com.zch.api.dto.user.LoginForm;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.common.domain.result.Response;
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

    @GetMapping("/captcha/image")
    public Response<CaptchaVO> getCaptcha() {
        return Response.success(userService.getCaptcha());
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginForm form) {
        return Response.judge(userService.login(form));
    }

    @PostMapping("/add")
    public Response addUser(@RequestBody User user) {
        return Response.judge(userService.addUser(user));
    }

}
