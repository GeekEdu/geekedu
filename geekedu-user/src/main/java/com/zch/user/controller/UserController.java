package com.zch.user.controller;

import com.zch.api.dto.user.UserDTO;
import com.zch.user.domain.po.User;
import com.zch.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("新增用户，除了管理员之外的教师或学员")
    @PostMapping("/add")
    public Long addUser(@RequestBody UserDTO userDTO) {
        // TODO
        return null;
    }

    @ApiOperation("更新用户信息，教师或学员")
    @PostMapping("/update/{id}")
    public void updateUser(@RequestBody UserDTO userDTO) {
        // TODO
    }

    @ApiOperation("完善登录用户信息")
    @PostMapping("/addLogin")
    public void addLoginUser() {
        // TODO
    }

}
