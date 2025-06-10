package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.StaffRating;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StaffInfoDTO {
    public interface BasicView {
    }

    public interface RatingView extends BasicView {
    }

    private Long userId;

    @JsonView({BasicView.class, UserInfoDTO.BasicView.class})
    private Long staffId;

    @JsonView(BasicView.class)
    private String staffName;

    private String phoneNumber;

    @JsonView(BasicView.class)
    private String avatar;

    @JsonView({BasicView.class, UserInfoDTO.BasicView.class})
    private String department;

    @JsonView({BasicView.class, UserInfoDTO.BasicView.class})
    private String post;

    @JsonView(UserInfoDTO.BasicView.class)
    private String jobDescription;

    @JsonView({BasicView.class, UserInfoDTO.BasicView.class})
    private BigDecimal averageRating;

    @JsonView(RatingView.class)
    private StaffRatingDTO userRating;

    public StaffInfoDTO(Staff staff) {
        userId = staff.getUser().getId();
        staffId = staff.getStaffId();
        staffName = staff.getUser().getUserName();
        phoneNumber = staff.getUser().getPhoneNumber();
        avatar = staff.getUser().getAvatar();
        department = staff.getDepartment();
        post = staff.getPost();
        jobDescription = staff.getJobDescription();
        averageRating = staff.getAverageRating();
    }

    // 设置评分的方法
    public void setUserRating(StaffRating staffRating) {
        this.userRating = new StaffRatingDTO(staffRating);
    }
}