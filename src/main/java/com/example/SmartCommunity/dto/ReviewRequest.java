package com.example.SmartCommunity.dto;

import lombok.Getter;

@Getter
public class ReviewRequest {
    private Long staffId;
    private Integer score;
    private String content;
}
