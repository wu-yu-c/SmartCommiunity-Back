package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.StaffRating;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StaffRatingDTO {
    @JsonView(StaffInfoDTO.RatingView.class)
    private int score;

    @JsonView(StaffInfoDTO.RatingView.class)
    private String comment;

    @JsonView(StaffInfoDTO.RatingView.class)
    private LocalDateTime rating_time;

    StaffRatingDTO(StaffRating staffRating) {
        this.score = staffRating.getScore();
        this.comment=staffRating.getContent();
        this.rating_time=staffRating.getRatingTime();
    }
}
