package com.example.SmartCommunity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 20, message = "用户名最长20个字符")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phoneNumber;

    @Size(min = 6, message = "密码长度至少为6位")
    private String password;
}
