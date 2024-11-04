package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil; // 导入 Sa-Token 的 StpUtil 类
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 注入 PasswordEncoder

    public String register(String username, String password, String userEmail) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(username) != null) {
            return "用户名已存在";
        }

        // 创建新用户并加密密码
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 加密密码
        // user.setPassword(password); // 使用原始密码，不加密
        user.setUserEmail(userEmail != null ? userEmail : ""); // 如果 userEmail 为空，则设置为空字符串

        userRepository.save(user); // 保存用户，自动生成 UserID
        return "注册成功";
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) { // 直接比较原始密码
            // 登录成功，生成一个 token
            StpUtil.login(user.getUserID()); // 使用 Sa-Token 的 login 方法

            return "登录成功，token: " + StpUtil.getTokenValue(); // 返回 token
        } else {
            return "用户名或密码错误,登录失败";
        }
    }
}
