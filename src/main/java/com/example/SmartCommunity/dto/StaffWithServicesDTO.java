package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class StaffWithServicesDTO {
    private Long staffId;
    private String name;
    private String avatar;
    private String position;
    private String department;
    private BigDecimal averageRating;
    private List<ServiceDTO> services;
}


