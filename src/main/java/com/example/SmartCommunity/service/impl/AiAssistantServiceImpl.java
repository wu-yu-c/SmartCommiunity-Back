package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.Resident;
import com.example.SmartCommunity.model.UserMessage;
import com.example.SmartCommunity.repository.ResidentRepository;
import com.example.SmartCommunity.repository.UserMessageRepository;
import com.example.SmartCommunity.service.AiAssistantService;
import com.example.SmartCommunity.util.AiResponseGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final AiResponseGenerator aiResponseGenerator;
    private final UserMessageRepository userMessageRepository;
    private final ResidentRepository residentRepository;

    public AiAssistantServiceImpl(@Qualifier("glmGenerator") AiResponseGenerator aiResponseGenerator,
                                  UserMessageRepository userMessageRepository,
                                  ResidentRepository residentRepository) {
        this.aiResponseGenerator = aiResponseGenerator;
        this.userMessageRepository = userMessageRepository;
        this.residentRepository = residentRepository;
    }

    @Override
    public String assistantResponse(String message, Integer userId) {
        return saveUserMessage(null, message);
//        return residentRepository.findById(userId)
//                .map(resident -> saveUserMessage(resident, message))
//                .orElse("resident not found");
    }

    private String saveUserMessage(Resident resident, String message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setResidentID(resident);
        userMessage.setMessageContent(message);
        userMessage.setSentAt(Instant.now());

        // userMessageRepository.save(userMessage);
        return aiResponseGenerator.generateResponse(message);
    }
}