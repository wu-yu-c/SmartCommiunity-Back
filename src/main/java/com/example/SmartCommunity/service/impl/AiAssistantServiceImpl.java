package com.example.SmartCommunity.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.example.SmartCommunity.controller.AiAssistantController;
import com.example.SmartCommunity.dto.ChatMessageDTO;
import com.example.SmartCommunity.dto.UserMessageDTO;
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
import com.example.SmartCommunity.util.OSSUtils;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final AiResponseGenerator aiResponseGenerator;
    private final UserMessageRepository userMessageRepository;
    private final AiMessageRepository aiMessageRepository;
    private final ChatTopicRepository chatTopicRepository;
    private final UserRepository userRepository;
    private final PagedResourcesAssembler<UserMessageDTO> pagedResourcesAssembler;

    public AiAssistantServiceImpl(@Qualifier("qwen2Generator") AiResponseGenerator aiResponseGenerator,
                                  UserMessageRepository userMessageRepository,
                                  AiMessageRepository aiMessageRepository,
                                  ChatTopicRepository chatTopicRepository,
                                  UserRepository userRepository,
                                  PagedResourcesAssembler<UserMessageDTO> pagedResourcesAssembler) {
        this.aiResponseGenerator = aiResponseGenerator;
        this.userMessageRepository = userMessageRepository;
        this.aiMessageRepository = aiMessageRepository;
        this.chatTopicRepository = chatTopicRepository;
        this.userRepository = userRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public String getAiResponse(Long topicId, String userMessageContent, String userImageContent) throws Exception {
        // Step 1: 检测 ChatTopic 是否存在
        if (!chatTopicRepository.existsById(topicId)) {
            throw new RuntimeException("ChatTopic not found");
        }

        // Step 2: 创建并保存 UserMessage
        UserMessage userMessage = new UserMessage();
        userMessage.setContentText(userMessageContent);
        userMessage.setContentImage(userImageContent);
        userMessage.setTopicID(chatTopicRepository.getReferenceById(topicId)); // 关联到 ChatTopic
        // Step 3: 调用 AI 服务生成 AI 回复
        String aiReplyContent = aiResponseGenerator.generateResponse(new ChatMessageDTO(userMessage)).getText();
        // 生成ai回复成功后再保存
        userMessageRepository.save(userMessage);

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

    private Page<UserMessage> getMessagesByTopicId(@NotNull Long topicId,
                                                   @NotNull Integer offset, @NotNull Integer limit) {
        ChatTopic chatTopic = chatTopicRepository.getReferenceById(topicId);
        Pageable pageable = PageRequest.of(offset / limit, limit,
                                            Sort.by(Sort.Direction.DESC, "id"));
        return userMessageRepository.findByTopicID(chatTopic, pageable);
    }

    @Override
    public PagedModel<EntityModel<UserMessageDTO>> getMessagesByTopicIdPaged(@NotNull Long topicId,
                                                                        @NotNull Integer offset,
                                                                        @NotNull Integer limit) {
        // 获取分页消息
        Page<UserMessage> messages = getMessagesByTopicId(topicId, offset, limit);
        // 转换为 DTO
        Page<UserMessageDTO> messageDTOs = messages.map(UserMessageDTO::new);

        // 使用 PagedResourcesAssembler 封装为 PagedModel
        return pagedResourcesAssembler.toModel(
                messageDTOs,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AiAssistantController.class)
                        .getMessagesByTopicId(topicId, offset, limit)).withSelfRel());
    }

    @Override
    public List<ChatTopic> getTopicsByUserId(@NotNull Long userId) {
        User user = userRepository.getReferenceById(userId);
        return chatTopicRepository.findByUserID(user);
    }

    @Override
    @Transactional
    public void deleteTopicById(@NotNull Long topicId) {
        ChatTopic chatTopic = chatTopicRepository.getReferenceById(topicId);
        List<UserMessage>messages_=userMessageRepository.findByTopicID(chatTopic);
        for(UserMessage message:messages_){
            String url=message.getContentImage();
            if(url.length()==0)continue;
            int lastIndex = url.lastIndexOf('/');
            // 提取文件名部分
            String fileName = url.substring(lastIndex + 1);
            try {
                OSSUtils.deleteFile(fileName);
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }
            System.out.println(message.getContentImage());
        }
        chatTopicRepository.deleteById(topicId);
    }

    @Override
    public void createTopic(@NotNull Long userId, @NotNull String topicName) {
        ChatTopic chatTopic = new ChatTopic();
        chatTopic.setUserID(userRepository.getReferenceById(userId));
        chatTopic.setTopicName(topicName);
        chatTopicRepository.save(chatTopic);
    }

    @Override
    public void updateTopicName(@NotNull Long topicId, @NotNull String newTopicName) {
        ChatTopic chatTopic = chatTopicRepository.getReferenceById(topicId);
        chatTopic.setTopicName(newTopicName);
        chatTopicRepository.save(chatTopic);
    }
}