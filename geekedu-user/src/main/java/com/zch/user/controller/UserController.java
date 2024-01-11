package com.zch.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.zch.common.domain.result.Response;
import com.zch.user.domain.form.LoginForm;
import com.zch.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @CrossOrigin
    @PostMapping("/login")
    public Response doLogin(@RequestBody LoginForm form) {
        userService.login(form);
        return Response.success();
    }

    @CrossOrigin
    @GetMapping("/captcha/image")
    public Response getCaptcha() {
        return Response.success(userService.getCaptcha());
    }

}
