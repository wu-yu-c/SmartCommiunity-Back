package com.example.SmartCommunity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    // Getters and Setters
    private Long userId;
    private String userName;
    private String phoneNumber;
    public UserDTO(Long id, String username, String phone) {
        this.userId = id;
        this.userName = username;
        this.phoneNumber = phone;
    }
}

