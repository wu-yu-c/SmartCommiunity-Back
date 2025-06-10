package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.ChatMessageDTO;
import com.example.SmartCommunity.model.ChatSession;

import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatSession session, Byte sender, Byte contentType, String content);

    List<ChatMessageDTO> getMessagesBySessionId(Long sessionId, Long userId);
}
