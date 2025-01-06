package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UserService {
    Map<String, Object> register(String username, String userPhone, String password);

    Map<String, Object> login(String username, String userPhone, String password);

    void changePassword(String username, String phone, String newPassword);

    Map<String, Object> getUserInfo(Long userId);

    Map<String, Object> updateUserInfo(Long userId, UserDTO userDTO);

    Map<String, Object> userAvatar(Long userId, MultipartFile file);
}