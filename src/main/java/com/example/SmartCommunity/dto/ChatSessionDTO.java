package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.ChatSession;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionDTO {
    private Long sessionId;
    private String sessionName;
    private LocalDateTime lastUpdatedTime;

    public ChatSessionDTO(ChatSession chatSession) {
        this.sessionId = chatSession.getId();
        this.sessionName = chatSession.getSessionName();
        this.lastUpdatedTime = chatSession.getLastUpdatedTime();
    }
}
