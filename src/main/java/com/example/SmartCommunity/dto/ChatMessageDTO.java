package com.example.SmartCommunity.dto;

import com.example.SmartCommunity.model.UserMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String role;
    private String image_url = null;
    private String text = null;

    public ChatMessageDTO(UserMessage userMessage) {
        this.role = "user";

        if (userMessage.getContentText() != null) {
            this.text = userMessage.getContentText();
        }

        if (userMessage.getContentImage() != null) {
            this.image_url = userMessage.getContentImage();
        }
    }
}
