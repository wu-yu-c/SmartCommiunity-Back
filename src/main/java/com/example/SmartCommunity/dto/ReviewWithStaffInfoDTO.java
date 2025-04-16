package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReviewWithStaffInfoDTO {
    private Long ratingId;
    private Integer score;
    private String content;
    private LocalDateTime ratingTime;
    private String staffName;
    private String avatar;
    private String department;
    private String post;
}

