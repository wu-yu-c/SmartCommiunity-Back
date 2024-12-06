package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.Admin;
import com.example.SmartCommunity.repository.AdminRepository;
import com.example.SmartCommunity.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.stp.StpUtil;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> register(String adminName, String adminPhone, String password) {
        Map<String, Object> response = new HashMap<>();

        // 输入合法性验证
        if (StringUtils.isBlank(adminName)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "管理员姓名不能为空");
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

        try {
            // 检查管理员手机号是否已存在
            if (adminRepository.findByAdminPhone(adminPhone) != null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "手机号已存在");
                return response;
            }

            if (adminRepository.findByAdminName(adminName) != null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "名称已存在");
                return response;
            }

            // 创建新管理员并加密密码
            Admin admin = new Admin();
            admin.setAdminName(adminName);
            admin.setPassword(passwordEncoder.encode(password)); // 加密密码
            admin.setAdminPhone(adminPhone);

            adminRepository.save(admin); // 保存管理员，自动生成 AdminID

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
    public Map<String, Object> login(String adminName, String adminPhone, String password) {
        Map<String, Object> response = new HashMap<>();

        // 输入合法性验证
        if (StringUtils.isBlank(adminName) && StringUtils.isBlank(adminPhone)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "用户名或手机号不能为空");
            return response;
        }
        if (StringUtils.isBlank(password)) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "密码不能为空");
            return response;
        }

        try {
            // 查找管理员
            Admin admin = null;

            if (StringUtils.isNotBlank(adminName)) {
                admin = adminRepository.findByAdminName(adminName);
                System.out.println("adminName: " + adminName);
            }
            if (admin == null && StringUtils.isNotBlank(adminPhone)) {
                admin = adminRepository.findByAdminPhone(adminPhone);
            }
            // 如果用户未找到
            if (admin == null) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "用户不存在");
                return response;
            }

            // 验证密码
            if (!passwordEncoder.matches(password, admin.getPassword())) {
                response.put("code", HttpStatus.BAD_REQUEST.value());
                response.put("message", "密码错误");
                return response;
            }

            // 登录成功，生成 token
            StpUtil.login(admin.getAdminID()); // 使用 Sa-Token 的 login 方法

            response.put("code", HttpStatus.OK.value());
            response.put("message", "登录成功");
            response.put("token", StpUtil.getTokenValue()); // 返回生成的 token
            return response;

        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈信息
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "发生未知错误，请稍后再试");
            return response;
        }
    }
}
