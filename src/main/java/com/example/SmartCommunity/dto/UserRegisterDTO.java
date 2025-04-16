package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegisterDTO {
    private Long userId;
    private String userName;
    private String phoneNumber;
    private String avatar;
    private LocalDateTime registerDate;

    public UserRegisterDTO(User user) {
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.avatar = user.getAvatar();
        this.registerDate = user.getCreatedTime();
    }
}

