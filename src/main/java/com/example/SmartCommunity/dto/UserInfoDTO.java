package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoDTO {
    public interface BasicView {
    }

    public interface UpdatedAvatarView {
    }

    public interface UpdatedInfoView extends BasicView {
    }

    @JsonView({BasicView.class, UpdatedAvatarView.class})
    private Long userId;

    @JsonView({BasicView.class, UpdatedAvatarView.class})
    private String userName;

    @JsonView(BasicView.class)
    private String phoneNumber;

    @JsonView({BasicView.class, UpdatedAvatarView.class})
    private String avatar;

    @JsonView(BasicView.class)
    private Byte userType;

    @JsonView({UpdatedInfoView.class, UpdatedAvatarView.class})
    private LocalDateTime updateTime;

    public UserInfoDTO(User user) {
        userId = user.getId();
        userName = user.getUserName();
        phoneNumber = user.getPhoneNumber();
        avatar = user.getAvatar();
        userType = user.getUserType();
        updateTime = user.getUpdatedTime();
    }
}
