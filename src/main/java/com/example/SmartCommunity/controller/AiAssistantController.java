package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.AiAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-assistant")
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    public AiAssistantController(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @PostMapping("/response")
    public ResponseEntity<String> getAssistantResponse(
            @RequestParam Long userId,
            @RequestBody String messageContent) {

        System.out.println("Message Content: " + messageContent);
        String response = aiAssistantService.assistantResponse(messageContent, userId);
        return ResponseEntity.ok(response);
    }
}
