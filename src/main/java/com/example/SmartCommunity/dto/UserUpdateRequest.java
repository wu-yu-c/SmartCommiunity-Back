package com.example.SmartCommunity.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(max = 20, message = "用户名最长20个字符")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phoneNumber;
}
