package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.UserDTO;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.UserService;
import com.example.SmartCommunity.util.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil; // 导入 Sa-Token 的 StpUtil 类
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> register(String username, String userPhone, String password) {
        Map<String, Object> response = new HashMap<>();

        // 输入合法性验证
        if (StringUtils.isBlank(username)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "用户名不能为空");
            return response;
        }
        if (StringUtils.isBlank(password)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "密码不能为空");
            return response;
        }
        if (password.length() < 6) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "密码长度必须大于等于6");
            return response;
        }

        // 电话号码格式验证
        if (StringUtils.isBlank(userPhone) || !Pattern.matches("^[0-9]{10,15}$", userPhone)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "电话号码格式不正确");
            return response;
        }

        try {
            // 检查用户名是否已存在
            if (userRepository.findByUsername(username) != null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "用户名已存在");
                return response;
            }
            if (userRepository.findByUserPhone(userPhone) != null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "该手机号已被注册");
                return response;
            }
            // 创建新用户并加密密码
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // 加密密码
            user.setUserPhone(userPhone); // 存储电话号码

            userRepository.save(user); // 保存用户，自动生成 UserID

            response.put("code", HttpStatus.OK.value());
            response.put("message", "注册成功");
            return response;

        } catch (DataAccessException e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "数据库操作失败，请稍后再试");
            return response;
        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

    @Override
    public Map<String, Object> login(String username, String userPhone, String password) {
        Map<String, Object> response = new HashMap<>();

        // 输入合法性验证
        if (StringUtils.isBlank(username) && StringUtils.isBlank(userPhone)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "用户名或电话号码不能为空");
            return response;
        }
        if (StringUtils.isBlank(password)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "密码不能为空");
            return response;
        }

        try {
            User user = null;

            // 根据输入查询用户
            if (StringUtils.isNotBlank(username)) {
                user = userRepository.findByUsername(username);
            }
            if (user == null && StringUtils.isNotBlank(userPhone)) {
                user = userRepository.findByUserPhone(userPhone);
            }

            // 如果用户未找到
            if (user == null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "用户不存在");
                return response;
            }

            // 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "密码错误");
                return response;
            }

            // 登录成功，使用 Sa-Token 生成 Token
            StpUtil.login(user.getUserID());

            response.put("code", HttpStatus.OK.value());
            response.put("message", "登录成功");
            response.put("userID", user.getUserID());
            response.put("token", StpUtil.getTokenValue());
            return response;

        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

    @Override
    public void changePassword(String username, String phone, String newPassword) {
        // 查找用户
        User user = (User) userRepository.findByUsernameAndUserPhone(username, phone)
                .orElseThrow(() -> new RuntimeException("User not found with the provided username and phone"));
        // 更新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    // 获取用户信息并转换为dto
    @Override
    public Map<String, Object> getUserInfo(Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "用户不存在");
                return response;
            } else {
                User user = optionalUser.get();
                response.put("code", HttpStatus.OK.value());
                response.put("data", new UserDTO(user.getUserID(), user.getUsername(), user.getUserPhone(),user.getAvatar()));
                return response;
            }
        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

    @Override
    public Map<String, Object> updateUserInfo(Long userId, UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 查找用户
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));

            Optional<User> repeatUser = Optional.ofNullable(userRepository.findByUsername(userDTO.getUserName()));
            if (repeatUser.isPresent() && !repeatUser.get().getUserID().equals(userId)) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "用户名已被使用");
                return response;
            }

            Optional<User> repeatUser2 = Optional.ofNullable(userRepository.findByUserPhone(userDTO.getPhoneNumber()));
            if (repeatUser2.isPresent() && !repeatUser2.get().getUserID().equals(userId)) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "该手机号已被使用");
                return response;
            }
            // 更新字段
            if (userDTO.getUserName() != null) {
                user.setUsername(userDTO.getUserName());
            }
            if (userDTO.getPhoneNumber() != null) {
                user.setUserPhone(userDTO.getPhoneNumber());
            }

            // 保存更新后的用户
            userRepository.save(user);

            response.put("code", HttpStatus.OK.value());
            response.put("message", "用户信息修改成功");
            return response;
        } catch (RuntimeException e) {
            response.put("code", HttpStatus.NOT_FOUND.value());
            response.put("message", e.getMessage());
            return response;
        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

    @Override
    public Map<String, Object> userAvatar(Long userId, MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 获取用户信息
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
            String defaultAvatar = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/Avatar/" +
                    "e6fc3672-f78c-4328-b93d-124f38a3aa35.jpg";

            // 2. 处理旧头像
            String oldAvatarUrl = user.getAvatar();
            if (oldAvatarUrl != null&&!oldAvatarUrl.equals(defaultAvatar)) {
                // 删除旧头像
                String oldAvatarKey = oldAvatarUrl.replace("https://first-tekcub.oss-cn-shanghai.aliyuncs.com/", "");
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
                result.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
                result.put("message", "Failed to upload new avatar");
                return result;
            }

            // 4. 更新用户信息
            String fileUrl = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/" + objectName;
            user.setAvatar(fileUrl);
            userRepository.save(user);
            result.put("code", HttpStatus.OK.value());
            result.put("message", "Avatar updated successfully");
            result.put("avatar", fileUrl);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
            result.put("message", e.getMessage());
            return result;
        }
    }
}