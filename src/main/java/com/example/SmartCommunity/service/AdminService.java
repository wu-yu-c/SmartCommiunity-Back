package com.example.SmartCommunity.service;

import java.util.Map;

public interface AdminService {

    /**
     * 管理员注册
     * 
     * @param adminName  管理员姓名
     * @param password   密码
     * @param adminPhone 管理员电话
     * @return 返回注册结果
     */
    Map<String, Object> register(String adminName, String password, String adminPhone);

    /**
     * 管理员登录
     * 
     * @param adminPhone 管理员电话
     * @param password   密码
     * @return 返回登录结果
     */
    Map<String, Object> login(String adminPhone, String password);

}
