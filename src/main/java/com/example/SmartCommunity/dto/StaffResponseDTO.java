package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.Staff;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class StaffResponseDTO {

    @JsonProperty("staffID")
    private Long ID;
    private String name;
    private String position;
    private String avatar;
    private String department;
    private BigDecimal averageRating;
    private Long areaID;

    public StaffResponseDTO(Staff staff) {
        this.ID = staff.getStaffID();
        this.name = staff.getName();
        this.position = staff.getPosition();
        this.avatar = staff.getAvatar();
        this.department=staff.getDepartment();
        this.averageRating=staff.getAverageRating();
        this.areaID =staff.getManagementArea().getAreaID();
    }

}
