package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.ChatSession;

public interface ChatMessageService {
    void saveMessage(ChatSession session, Byte sender, Byte contentType, String content);
}
