package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.ChatSessionDTO;

import java.util.List;

public interface ChatSessionService {
    void deleteSessionById(Long sessionId);

    List<ChatSessionDTO> getUserChatSessions();
}
