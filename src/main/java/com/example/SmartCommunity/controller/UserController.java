package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.UserService;
import com.example.SmartCommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String result = userService.register(user.getUsername(), user.getPassword(), user.getUserEmail());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String result = userService.login(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(result);
    }
}
