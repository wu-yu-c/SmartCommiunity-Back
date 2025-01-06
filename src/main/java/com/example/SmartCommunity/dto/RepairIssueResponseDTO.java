package com.example.SmartCommunity.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class RepairIssueResponseDTO {
    private Long id;
    private Long residentID;
    private String repairIssueDetails;
    private String repairIssueStatus;
    private Instant repairIssueStart;
    private String repairAddress;
    private String repairIssueTitle;
    private String repairIssueCategory;
    private String imageUrl;
    private String videoUrl;
}