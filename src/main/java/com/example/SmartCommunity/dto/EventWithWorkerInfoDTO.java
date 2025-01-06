package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
public class EventWithWorkerInfoDTO {
    private Long eventId;
    private String description;
    private Integer score;
    private String comment;
    private Timestamp createdTime;
    private String workerName;
    private String workerAvatar;
    private String workerPosition;
    private String workerDepartment;

    public EventWithWorkerInfoDTO(Long eventId, String description, Integer score, String comment,
                                  Timestamp createdTime, String workerName, String workerAvatar,
                                  String workerPosition, String workerDepartment) {
        this.eventId = eventId;
        this.description = description;
        this.score = score;
        this.comment = comment;
        this.createdTime = createdTime;
        this.workerName = workerName;
        this.workerAvatar = workerAvatar;
        this.workerPosition = workerPosition;
        this.workerDepartment = workerDepartment;
    }
}

