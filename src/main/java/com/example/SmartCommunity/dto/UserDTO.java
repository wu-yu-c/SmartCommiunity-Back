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
    private String avatar;

    public UserDTO(Long id, String username, String phone, String avatar) {
        this.userId = id;
        this.userName = username;
        this.phoneNumber = phone;
        this.avatar = avatar;
    }
}

