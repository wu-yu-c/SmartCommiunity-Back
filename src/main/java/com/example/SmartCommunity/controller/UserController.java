package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.UserService;
import com.example.SmartCommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Map<String, Object> result = userService.register(user.getUsername(), user.getPassword(), user.getUserEmail());
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> result = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.status((Integer) result.get("code")).body(result);
    }
}