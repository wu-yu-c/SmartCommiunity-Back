package com.example.SmartCommunity.enums;

import lombok.Getter;

@Getter
public enum RepairStatusType {
    PENDING("待处理"),
    IN_PROGRESS("处理中"),
    COMPLETED("已完成"),
    REJECTED("已驳回");

    private final String description;

    RepairStatusType(String description) {
        this.description = description;
    }
}

