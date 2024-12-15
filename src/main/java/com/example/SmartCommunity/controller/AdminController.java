package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "管理员接口")
@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "管理员注册接口", description = "通过管理员名称、手机号和密码进行注册")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String adminName, @RequestParam String adminPhone, @RequestParam String password) {
        Map<String, Object> result = adminService.register(adminName, adminPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "管理员登录接口", description = "支持使用管理员名称或手机号配合密码进行登录")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam(required = false) String adminName,
            @RequestParam(required = false) String adminPhone, @RequestParam String password) {
        Map<String, Object> result = adminService.login(adminName, adminPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }
}