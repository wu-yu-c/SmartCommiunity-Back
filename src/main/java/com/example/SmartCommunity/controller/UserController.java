package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String username, @RequestParam String password,
            @RequestParam(required = false) String userEmail) {
        Map<String, Object> result = userService.register(username, password, userEmail);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(String username, String password) {
        Map<String, Object> result = userService.login(username, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }
}
