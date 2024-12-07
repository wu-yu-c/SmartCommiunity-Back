package com.example.SmartCommunity.service;

import java.util.Map;

public interface AdminService {

    Map<String, Object> register(String adminName, String adminPhone, String password);

    Map<String, Object> login(String adminName, String adminPhone, String password);

}
