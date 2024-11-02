package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-assistant")
@CrossOrigin(origins = "http://localhost:9000")
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @Autowired
    public AiAssistantController(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @PostMapping("/response")
    public ResponseEntity<String> getAssistantResponse(
            @RequestParam Integer residentId,
            @RequestBody String messageContent) {

        System.out.println("Message Content: " + messageContent);
        String response = aiAssistantService.assistantResponse(messageContent, residentId);
        return ResponseEntity.ok(response);
    }
}
