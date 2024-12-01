package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "管理员注册接口")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String adminName, @RequestParam String password, @RequestParam String adminPhone) {
        Map<String, Object> result = adminService.register(adminName, password, adminPhone);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "管理员登录接口")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String adminPhone, @RequestParam String password) {
        Map<String, Object> result = adminService.login(adminPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }
}
