package com.david.sys.controller;

import com.david.common.vo.Result;
import com.david.sys.entity.User;
import com.david.sys.service.IUserService;
import com.david.sys.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author david
 * @since 2024-03-25
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    IUserService userService;

    @GetMapping("/all")
    public Result<List<User>> getAll() {
        List<User> users = userService.list();
        return Result.success(users, "success");
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if(data != null) {
            return Result.success(data);
        }
        return Result.fail(20002, "用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> login(@RequestParam("token") String token) {
        Map<String, Object> data = userService.getUserInfo(token);
        if(data != null) {
            return Result.success(data);
        }
        return Result.fail(20003, "token无效");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }
}
