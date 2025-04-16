package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserRegisterDTO register(UserRegisterRequest request);

    UserLoginDTO login(String username, String phoneNumber, String password);

    UserInfoDTO getUserInfo();

    void changePassword(String username, String phone, String newPassword);

    UserInfoDTO updateUserInfo(UserUpdateRequest updateRequest);

    UserInfoDTO userAvatar(Long userId, MultipartFile file);
}