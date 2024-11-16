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

    // 正则表达式用于验证邮箱格式
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public Map<String, Object> register(String username, String password, String userEmail) {
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
        if (StringUtils.isNotBlank(userEmail) && !Pattern.matches(EMAIL_PATTERN, userEmail)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "邮箱格式不正确");
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
            user.setUserEmail(userEmail != null ? userEmail : ""); // 如果 userEmail 为空，则设置为空字符串

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
    public Map<String, Object> login(String username, String password) {
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

        try {
            User user = userRepository.findByUsername(username);

            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                // 登录成功，生成 token
                StpUtil.login(user.getUserID()); // 使用 Sa-Token 的 login 方法

                response.put("code", HttpStatus.OK.value());
                response.put("message", "登录成功");
                response.put("token", StpUtil.getTokenValue()); // 返回生成的 token
                return response;
            } else {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "用户名或密码错误,登录失败");
                return response;
            }

        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }
}