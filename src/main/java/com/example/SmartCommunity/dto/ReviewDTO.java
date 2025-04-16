package com.example.SmartCommunity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long raterId;
    private Integer score;
    private String content;
    private LocalDateTime ratingTime;

    public ReviewDTO(Long id, Long raterId, Integer score, String content, LocalDateTime ratingTime) {
        this.id = id;
        this.raterId = raterId;
        this.score = score;
        this.content = content;
        this.ratingTime = ratingTime;
    }
}
