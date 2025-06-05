package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.enums.RepairStatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitRepairResultRequest {
    @NotNull
    private Long issueId;

    private String completionNote;

    @NotNull(message = "处理状态不能为空")
    private RepairStatusType status;
}

