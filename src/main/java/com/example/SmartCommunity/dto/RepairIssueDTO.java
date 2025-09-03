package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.enums.RepairStatusType;
import com.example.SmartCommunity.model.RepairIssue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairIssueDTO implements Serializable {
    public interface BasicView {}

    @JsonView(BasicView.class)
    private Long issueId;

    @JsonView(BasicView.class)
    private String reporter;

    @JsonView(BasicView.class)
    private String issueTitle;

    @JsonView(BasicView.class)
    private String issueDescription;

    @JsonView(BasicView.class)
    private String category;

    @JsonView(BasicView.class)
    private String location;

    @JsonView(BasicView.class)
    private RepairStatusType status;

    @JsonView(BasicView.class)
    private LocalDateTime issueDate;

    private String imageUrl;
    private String videoUrl;
    private String assigned;
    private LocalDateTime completedDate;
    private String completionNote;

    public RepairIssueDTO(){}

    public RepairIssueDTO(RepairIssue repairIssue) {
        issueId = repairIssue.getId();
        reporter = repairIssue.getReporter().getUserName();
        issueTitle = repairIssue.getIssueTitle();
        issueDescription = repairIssue.getIssueDescription();
        category = repairIssue.getCategory().getCategoryName();
        location = repairIssue.getLocation();
        status = repairIssue.getStatus();
        issueDate = repairIssue.getCreatedTime();
        imageUrl = repairIssue.getImageUrl();
        videoUrl = repairIssue.getVideoUrl();
        assigned = repairIssue.getAssigned() == null ? null : repairIssue.getAssigned().getUser().getUserName();
        completedDate = repairIssue.getCompletedTime();
        completionNote = repairIssue.getCompletionNote();
    }
}