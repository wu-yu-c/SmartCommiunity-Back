package com.example.SmartCommunity.util;

import com.zhipu.oapi.service.v4.model.ChatMessage;

public interface AiResponseGenerator {
    ChatMessage generateResponse(String message);
}