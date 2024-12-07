package com.example.SmartCommunity.service;

import java.util.Map;

public interface UserService {
    Map<String, Object> register(String username, String userPhone, String password);

    Map<String, Object> login(String username, String userPhone, String password);
}