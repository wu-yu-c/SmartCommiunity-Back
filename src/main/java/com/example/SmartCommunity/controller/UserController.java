package com.example.SmartCommunity.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.dto.*;
import com.example.SmartCommunity.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册", description = "用户通过用户名、手机号与密码注册，用户名与手机号必须唯一")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterDTO>> register(
            @Valid @RequestBody UserRegisterRequest request) {
        UserRegisterDTO response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "注册成功", response));
    }

    @Operation(summary = "用户登录", description = "支持通过用户名或手机号任意一种方式登录")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginDTO>> login(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam @NotBlank(message = "密码不能为空") String password) {
        UserLoginDTO response = userService.login(username, phoneNumber, password);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "登录成功", response));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        if (StpUtil.isLogin())
            StpUtil.logout();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "退出登录成功", null));
    }

    @Operation(summary = "获取个人信息", description = "返回该用户的有关信息")
    @SaCheckLogin
    @GetMapping("/user-info")
    @JsonView(UserInfoDTO.BasicView.class)
    public ResponseEntity<ApiResponse<UserInfoDTO>> getUserInfo() {
        UserInfoDTO response = userService.getUserInfo();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "用户信息获取成功", response));
    }

    @Operation(summary = "更新个人信息", description = "返回修改成功与否以及修改后的结果")
    @SaCheckLogin
    @PutMapping("/user-info")
    @JsonView(UserInfoDTO.UpdatedInfoView.class)
    public ResponseEntity<ApiResponse<UserInfoDTO>> updateUserInfo(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserInfoDTO response = userService.updateUserInfo(updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "用户信息更新成功", response));
    }

    @Operation(summary = "修改用户密码", description = "用户和职工都通过该接口修改密码")
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestParam String name,
                                                            @RequestParam String phone,
                                                            @RequestParam @Size(min = 6, message = "密码的最小长度应为6位") String newPassword) {
        userService.changePassword(name, phone, newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "密码修改成功", null));
    }

    @Operation(summary = "上传或修改用户头像", description = "通过用户ID上传头像，如果已有头像的话就修改头像")
    @PutMapping(value = "/avatar", consumes = "multipart/form-data")
    @SaCheckLogin
    @JsonView(UserInfoDTO.UpdatedAvatarView.class)
    public ResponseEntity<ApiResponse<UserInfoDTO>> updateAvatar(@RequestParam("file") MultipartFile file) {
        UserInfoDTO response = userService.userAvatar(file);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "头像上传成功", response));
    }
}