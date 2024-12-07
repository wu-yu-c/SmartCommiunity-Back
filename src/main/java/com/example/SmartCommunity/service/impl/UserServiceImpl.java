package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil; // 导入 Sa-Token 的 StpUtil 类
import java.util.HashMap;
import java.util.Map;
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
            response.put("token", StpUtil.getTokenValue());
            return response;

        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }

}