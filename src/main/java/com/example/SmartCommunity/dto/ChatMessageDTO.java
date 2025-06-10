package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.ChatMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private Long messageId;
    private String senderType;
    private String content;
    private LocalDateTime created_time;

    public ChatMessageDTO(ChatMessage chatMessage) {
        this.messageId = chatMessage.getId();
        this.senderType = chatMessage.getSender() == 1 ? "user" : "assistant";
        this.content = chatMessage.getContent();
        this.created_time = chatMessage.getCreatedTime();
    }
}
