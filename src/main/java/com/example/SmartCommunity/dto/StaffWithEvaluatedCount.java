package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StaffWithEvaluatedCount {
    private Long staffId;
    private String name;
    private String avatar;
    private String position;
    private String department;
    private BigDecimal averageRating;
    private Long evaluationCount= 0L;  // 某职工被评价次数
}
