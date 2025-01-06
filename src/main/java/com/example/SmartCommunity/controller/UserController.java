package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.UserDTO;
import com.example.SmartCommunity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户注册接口", description = "用户通过用户名、手机号与密码注册，用户名与手机号必须唯一")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String username,
            @RequestParam String userPhone,
            @RequestParam String password) {
        Map<String, Object> result = userService.register(username, userPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "用户登录接口", description = "支持通过用户名或手机号任意一种方式登录")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String userPhone,
            @RequestParam String password) {

        // 调用服务层进行用户登录，传入 username 或 userPhone 和密码
        Map<String, Object> result = userService.login(username, userPhone, password);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "获取个人信息接口", description = "返回该用户的有关信息")
    @GetMapping("/getUserInfo/{userId}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Long userId) {
        Map<String, Object> result = userService.getUserInfo(userId);
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @Operation(summary = "修改个人信息接口", description = "返回修改成功与否的结果")
    @PutMapping("/updateUserInfo/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO) {
        Map<String, Object> response = userService.updateUserInfo(userId, userDTO);
        return ResponseEntity.status((Integer) response.get("code")).body(response);
    }

    @Operation(summary = "修改用户密码接口")
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String name,
                                                 @RequestParam String phone,
                                                 @RequestParam String newPassword) {
        try {
            userService.changePassword(name, phone, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary="上传或修改用户头像接口",description = "通过用户ID上传头像，如果已有头像的话就修改头像")
    @PutMapping(value = "/avatar/{userID}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateAvatar(
            @PathVariable("userID") Long userID,
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = userService.userAvatar(userID,file);
        return ResponseEntity.status((Integer) response.get("code")).body(response);
    }
}