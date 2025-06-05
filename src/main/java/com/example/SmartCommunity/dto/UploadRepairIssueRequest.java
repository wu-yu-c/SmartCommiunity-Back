package com.example.SmartCommunity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class UploadRepairIssueRequest {
    @NotBlank(message="事件标题不能为空")
    private String issueTitle;

    @NotBlank(message="事件描述不能为空")
    private String issueDescription;

    @NotBlank(message="位置不能为空")
    private String location;

    @NotNull(message = "事件类别不能为空")
    private Long categoryId;
}