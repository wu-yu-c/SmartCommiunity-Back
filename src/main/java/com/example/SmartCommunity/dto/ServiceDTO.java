package com.example.SmartCommunity.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ServiceDTO {
    private Long eventId;
    private Long residentId;
    private String details;
    private String description;
    private Integer score;
    private String comment;
    private Timestamp evaluatedTime;

    public ServiceDTO(Long eventId,Long residentId,String details, String description, Integer score, String comment, Timestamp evaluatedTime) {
        this.eventId = eventId;
        this.residentId = residentId;
        this.details = details;
        this.description = description;
        this.score = score;
        this.comment = comment;
        this.evaluatedTime = evaluatedTime;
    }
}
