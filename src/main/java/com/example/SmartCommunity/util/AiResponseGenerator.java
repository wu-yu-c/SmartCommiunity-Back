package com.example.SmartCommunity.util;

import com.example.SmartCommunity.dto.ChatMessageDTO;

public interface AiResponseGenerator {
    ChatMessageDTO generateResponse(ChatMessageDTO message) throws Exception;
}