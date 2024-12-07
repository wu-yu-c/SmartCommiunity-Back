package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.UserMessage;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.repository.UserMessageRepository;
import com.example.SmartCommunity.service.AiAssistantService;
import com.example.SmartCommunity.util.AiResponseGenerator;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final AiResponseGenerator aiResponseGenerator;
    private final UserMessageRepository userMessageRepository;
    private final UserRepository userRepository; // 替换 ResidentRepository 为 UserRepository

    public AiAssistantServiceImpl(
            @Qualifier("glmGenerator") AiResponseGenerator aiResponseGenerator,
            UserMessageRepository userMessageRepository,
            UserRepository userRepository) {
        this.aiResponseGenerator = aiResponseGenerator;
        this.userMessageRepository = userMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String assistantResponse(String message, Long userId) {
        return userRepository.findById(userId.longValue()) // 使用 UserRepository 查询 User
                .map(user -> saveUserMessage(user, message))
                .orElse("User not found");
    }

    private String saveUserMessage(User user, String message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setUser(user); // 设置 User
        userMessage.setMessageContent(message);
        userMessage.setSentAt(Instant.now());

        userMessageRepository.save(userMessage);
        ChatMessage response = aiResponseGenerator.generateResponse(message);
        var result = response.getContent();
        if (result instanceof String) {
            return (String) result;
        }
        return "ai生成的内容不是文本，请更换模型";
    }
}