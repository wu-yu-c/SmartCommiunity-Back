package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.UserMessageDTO;
import com.example.SmartCommunity.dto.UserMessageRequestDTO;
import com.example.SmartCommunity.model.ChatTopic;
import com.example.SmartCommunity.model.UserMessage;
import com.example.SmartCommunity.service.AiAssistantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Tag(name="AI助手接口")
@RestController
@RequestMapping("/api/ai-assistant")
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @Autowired
    public AiAssistantController(AiAssistantService aiAssistantService) {
        this.aiAssistantService = aiAssistantService;
    }

    @PostMapping("/process-message")
    public ResponseEntity<String> processMessage(
            @RequestParam Long topicId,
            @RequestBody UserMessageRequestDTO userMessageRequestDTO) {
        try {
            String userMessageContent = userMessageRequestDTO.getUserMessageContent();
            String userImageContent = userMessageRequestDTO.getUserImageContent();
            if (userMessageContent == null && userImageContent == null) {
                return ResponseEntity.badRequest().body("User content is required.");
            }
            String response = aiAssistantService.getAiResponse(topicId, userMessageContent, userImageContent);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing message: "
                                                                                + e.getMessage());
        }
    }

    @GetMapping("/messages/{topicId}")
    public ResponseEntity<PagedModel<EntityModel<UserMessageDTO>>> getMessagesByTopicId(
            @PathVariable Long topicId,
            @RequestParam @NotNull Integer offset,
            @RequestParam @NotNull Integer limit) {
        try {
            // 调用 Service 获取封装好的 PagedModel
            PagedModel<EntityModel<UserMessageDTO>> pagedModel =
                    aiAssistantService.getMessagesByTopicIdPaged(topicId, offset, limit);
            return ResponseEntity.ok(pagedModel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/topics/{userId}")
    public ResponseEntity<List<ChatTopic>> getTopicsByUserId(@PathVariable Long userId) {
        try {
            List<ChatTopic> topics = aiAssistantService.getTopicsByUserId(userId);
            return ResponseEntity.ok(topics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/topic/{topicId}")
    public ResponseEntity<String> deleteTopicById(@PathVariable Long topicId) {
        try {
            aiAssistantService.deleteTopicById(topicId);
            return ResponseEntity.ok("Topic deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting topic: " + e.getMessage());
        }
    }

    @PostMapping("/topic")
    public ResponseEntity<String> createTopic(
            @RequestParam @NotNull Long userId,
            @RequestParam @NotNull String topicName) {
        try {
            aiAssistantService.createTopic(userId, topicName);
            return ResponseEntity.ok("Topic created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating topic: " + e.getMessage());
        }
    }

    @PutMapping("/topic/{topicId}")
    public ResponseEntity<String> updateTopicName(
            @PathVariable Long topicId,
            @RequestParam @NotNull String newTopicName) {
        try {
            aiAssistantService.updateTopicName(topicId, newTopicName);
            return ResponseEntity.ok("Topic updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating topic: " + e.getMessage());
        }
    }
}