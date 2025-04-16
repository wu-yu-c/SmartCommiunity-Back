package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.*;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.UserService;
import com.example.SmartCommunity.util.OSSUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserRegisterDTO register(UserRegisterRequest request) {
        if (userRepository.existsUserByUserName(request.getUsername())) {
            throw new IllegalArgumentException("用户名已被占用");
        }
        if (userRepository.existsUserByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("手机号已被注册");
        }

        User user = new User();
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密密码
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user); // 保存用户

        return new UserRegisterDTO(user);
    }

    @Override
    public UserLoginDTO login(String username, String phoneNumber, String password) {
        if (StringUtils.isBlank(username) && StringUtils.isBlank(phoneNumber)) {
            throw new IllegalArgumentException("用户名和手机号不能同时为空");
        }

        User user = userRepository.findUserByUserNameOrPhoneNumber(username, phoneNumber);

        if (user == null) {
            throw new NoSuchElementException("用户不存在");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        // 登录成功，使用 Sa-Token 生成 Token
        StpUtil.login(user.getId());

        return new UserLoginDTO(user, StpUtil.getTokenValue());
    }

    @Override
    public UserInfoDTO getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userRepository.findUserById(userId);
        return new UserInfoDTO(user);
    }

    @Override
    @Transactional
    public UserInfoDTO updateUserInfo(UserUpdateRequest updateRequest) {
        User user = userRepository.findUserById(StpUtil.getLoginIdAsLong());

        if (updateRequest.getUserName() != null && !updateRequest.getUserName().equals(user.getUserName())) {
            if (userRepository.existsUserByUserName(updateRequest.getUserName())) {
                throw new IllegalArgumentException("用户名已被使用");
            }
            user.setUserName(updateRequest.getUserName());
        }

        if (updateRequest.getPhoneNumber() != null && !updateRequest.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsUserByPhoneNumber(updateRequest.getPhoneNumber())) {
                throw new IllegalArgumentException("手机号已被使用");
            }
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        userRepository.save(user);
        entityManager.flush();
        User updatedUser = userRepository.findUserById(user.getId());
        return new UserInfoDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(String username, String phone, String newPassword) {
        User user = userRepository.findUserByUserNameAndPhoneNumber(username, phone)
                .orElseThrow(() -> new NoSuchElementException("未找到指定用户名和电话号码的用户"));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserInfoDTO userAvatar(Long userId, MultipartFile file) {
        // 1. 获取用户信息
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("用户不存在"));
        String defaultAvatar = "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/Avatar/defaultAvatar.jpg";

        try {
            // 2. 处理旧头像
            String oldAvatarUrl = user.getAvatar();
            if (oldAvatarUrl != null && !oldAvatarUrl.equals(defaultAvatar)) {
                // 删除旧头像
                String oldAvatarKey = oldAvatarUrl.replace("https://1st-bucket.oss-cn-shanghai.aliyuncs.com/", "");
                OSSUtils.deleteFile(oldAvatarKey);
            }

            // 3. 上传新头像
            // 使用 UUID 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;
            String objectName = "Avatar/" + uniqueFileName;
            String newAvatarUrl = OSSUtils.uploadFileToOSS(file, objectName);

            if (newAvatarUrl.equals("failure")) {
                throw new RuntimeException("上传新头像失败");
            }

            // 4. 更新用户信息
            String fileUrl = "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/" + objectName;
            user.setAvatar(fileUrl);
            userRepository.save(user);

            return new UserInfoDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("更新头像失败，请稍后重试");
        }
    }
}