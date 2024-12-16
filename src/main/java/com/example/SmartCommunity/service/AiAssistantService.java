package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.UserMessageDTO;
import com.example.SmartCommunity.model.ChatTopic;
import com.example.SmartCommunity.model.UserMessage;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AiAssistantService {
    @Transactional
    String getAiResponse(Long topicId, String userMessageContent, String userImageContent) throws Exception;

    PagedModel<EntityModel<UserMessageDTO>> getMessagesByTopicIdPaged(@NotNull Long topicId,
                                                                 @NotNull Integer offset,
                                                                 @NotNull Integer limit);

    List<ChatTopic> getTopicsByUserId(@NotNull Long userId);

    @Transactional
    void deleteTopicById(@NotNull Long topicId);

    @Transactional
    void createTopic(@NotNull Long userId, @NotNull String topicName);

    @Transactional
    void updateTopicName(@NotNull Long topicId, @NotNull String newTopicName);

}
