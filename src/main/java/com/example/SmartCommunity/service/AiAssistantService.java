package com.example.SmartCommunity.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public interface AiAssistantService {
    String assistantResponse(@NotNull String messageContent, @NotNull Long userId);
}
