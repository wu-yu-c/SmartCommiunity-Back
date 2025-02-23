package com.example.SmartCommunity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepairIssueDTO {
    private Long residentID;
    private String repairIssueDetails;
    private String repairAddress;
    private String repairIssueTitle;
    private String repairIssueCategory;
    private String repairIssueStatus;
}