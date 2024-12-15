package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册接口",description = "用户通过用户名、手机号与密码注册，用户名与密码必须唯一")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String username,
            @RequestParam String userPhone,
            @RequestParam String password) {
        Map<String, Object> result = userService.register(username, userPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "用户登录接口",description = "支持通过用户名或手机号任意一种方式登录")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String userPhone,
            @RequestParam String password) {

        // 调用服务层进行用户登录，传入 username 或 userPhone 和密码
        Map<String, Object> result = userService.login(username, userPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }
}