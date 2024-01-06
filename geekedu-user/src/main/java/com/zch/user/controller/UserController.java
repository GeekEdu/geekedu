package com.zch.user.controller;

import com.zch.user.domain.po.User;
import com.zch.user.service.IUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户接口")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return "OK : " + userService.insertUser(user);
    }

}
