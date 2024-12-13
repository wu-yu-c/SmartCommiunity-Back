package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.ChatMessageDTO;
import com.example.SmartCommunity.model.AiMessage;
import com.example.SmartCommunity.model.ChatTopic;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.model.UserMessage;
import com.example.SmartCommunity.repository.AiMessageRepository;
import com.example.SmartCommunity.repository.ChatTopicRepository;
import com.example.SmartCommunity.repository.UserMessageRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.AiAssistantService;
import com.example.SmartCommunity.util.AiResponseGenerator;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final AiResponseGenerator aiResponseGenerator;
    private final UserMessageRepository userMessageRepository;
    private final AiMessageRepository aiMessageRepository;
    private final ChatTopicRepository chatTopicRepository;
    private final UserRepository userRepository;

    public AiAssistantServiceImpl(@Qualifier("qwen2Generator") AiResponseGenerator aiResponseGenerator,
                                  UserMessageRepository userMessageRepository,
                                  AiMessageRepository aiMessageRepository, ChatTopicRepository chatTopicRepository, UserRepository userRepository) {
        this.aiResponseGenerator = aiResponseGenerator;
        this.userMessageRepository = userMessageRepository;
        this.aiMessageRepository = aiMessageRepository;
        this.chatTopicRepository = chatTopicRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public String getAiResponse(Long topicId, String userMessageContent, String userImageContent) {
        // Step 1: 检测 ChatTopic 是否存在
        if (!chatTopicRepository.existsById(topicId)) {
            throw new RuntimeException("ChatTopic not found");
        }

        // Step 2: 创建并保存 UserMessage
        UserMessage userMessage = new UserMessage();
        userMessage.setContentText(userMessageContent);
        userMessage.setContentImage(userImageContent);
        userMessage.setTopicID(chatTopicRepository.getReferenceById(topicId)); // 关联到 ChatTopic
        userMessageRepository.save(userMessage);

        // Step 3: 调用 AI 服务生成 AI 回复
        String aiReplyContent = aiResponseGenerator.generateResponse(new ChatMessageDTO(userMessage)).getText();

        // Step 4: 创建并保存 AiMessage
        AiMessage aiMessage = new AiMessage();
        aiMessage.setUserMessage(userMessage); // 关联到 UserMessage
        aiMessage.setContentText(aiReplyContent);
        aiMessage = aiMessageRepository.save(aiMessage); // 保存后获取生成的 ID

        // Step 5: 将 AI 回复关联到 UserMessage
        userMessage.getAiMessages().add(aiMessage); // 确保 userMessage 的集合已初始化
        userMessageRepository.save(userMessage);

        System.out.println("Message and AI reply saved successfully.");
        return aiReplyContent;
    }

    @Override
    public Page<UserMessage> getMessagesByTopicId(@NotNull Long topicId, @NotNull Integer offset, @NotNull Integer limit) {
        ChatTopic chatTopic = chatTopicRepository.getReferenceById(topicId);
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return userMessageRepository.findByTopicID(chatTopic, pageable);
    }

    @Override
    public List<ChatTopic> getTopicsByUserId(@NotNull Long userId) {
        User user = userRepository.getReferenceById(userId);
        return chatTopicRepository.findByUserID(user);
    }

    @Override
    @Transactional
    public void deleteTopicById(@NotNull Long topicId) {
        chatTopicRepository.deleteById(topicId);
    }

    @Override
    public void createTopic(@NotNull Long userId, @NotNull String topicName) {
        ChatTopic chatTopic = new ChatTopic();
        chatTopic.setUserID(userRepository.getReferenceById(userId));
        chatTopic.setTopicName(topicName);
        chatTopicRepository.save(chatTopic);
    }
}