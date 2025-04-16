package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    private Long userId;
    private String userName;
    private String phoneNumber;
    private String avatar;
    private Byte userType;
    private String token;

    public UserLoginDTO(User user,String token) {
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.avatar = user.getAvatar();
        this.userType = user.getUserType();
        this.token = token;
    }
}