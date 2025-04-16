package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.Staff;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StaffInfoDTO {
    public interface BasicView{}

    private Long userId;

    @JsonView(BasicView.class)
    private Long staffId;

    @JsonView(BasicView.class)
    private String staffName;

    private String phoneNumber;

    @JsonView(BasicView.class)
    private String avatar;

    @JsonView(BasicView.class)
    private String department;

    @JsonView(BasicView.class)
    private String post;

    private String jobDescription;

    @JsonView(BasicView.class)
    private BigDecimal averageRating;

    public StaffInfoDTO(Staff staff) {
        userId = staff.getUser().getId();
        staffId = staff.getId();
        staffName = staff.getUser().getUserName();
        phoneNumber = staff.getUser().getPhoneNumber();
        avatar = staff.getUser().getAvatar();
        department = staff.getDepartment();
        post = staff.getPost();
        jobDescription = staff.getJobDescription();
        averageRating = staff.getAverageRating();
    }
}
