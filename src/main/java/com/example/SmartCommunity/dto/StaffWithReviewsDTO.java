package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StaffWithReviewsDTO {
    private Long staffId;
    private String name;
    private String avatar;
    private String department;
    private String post;
    private BigDecimal averageRating;
    private List<ReviewDTO> reviews;

    public StaffWithReviewsDTO(Staff staff, List<ReviewDTO> reviews) {
        User user = staff.getUser();
        this.staffId = staff.getStaffId();
        this.name = user.getUserName();
        this.avatar = user.getAvatar();
        this.department = staff.getDepartment();
        this.post = staff.getPost();
        this.averageRating = staff.getAverageRating();
        this.reviews = reviews;
    }
}


