package com.example.SmartCommunity.common.response;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.StaffInfoDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;
import com.example.SmartCommunity.dto.UserInfoDTO;
import com.fasterxml.jackson.annotation.JsonView;

@Data
@JsonView({UserInfoDTO.BasicView.class,UserInfoDTO.UpdatedInfoView.class,UserInfoDTO.UpdatedAvatarView.class,
        StaffInfoDTO.BasicView.class, RepairIssueDTO.BasicView.class,StaffInfoDTO.RatingView.class})
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, String msg, T data) {
        this.code = status.value();
        this.message = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String msg, T data) {
        return new ApiResponse<>(status, msg, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String msg, T data) {
        return new ApiResponse<>(status, msg, data);
    }
}
