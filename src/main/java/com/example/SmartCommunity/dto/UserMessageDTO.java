package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.AiMessage;
import com.example.SmartCommunity.model.UserMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserMessageDTO {
    private Long id;
    private String contentText;
    private String contentImage;
    private Instant createTime;
    private List<String> aiResponses;

    public UserMessageDTO(UserMessage userMessage) {
        this.id = userMessage.getId();
        this.contentText = userMessage.getContentText();
        this.contentImage = userMessage.getContentImage();
        this.createTime = userMessage.getCreateTime();
        this.aiResponses = userMessage.getAiMessages()
                .stream()
                .map(AiMessage::getContentText)
                .toList();
    }
}
