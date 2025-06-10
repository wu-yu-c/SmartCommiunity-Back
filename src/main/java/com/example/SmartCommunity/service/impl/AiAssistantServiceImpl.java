package com.example.SmartCommunity.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.example.SmartCommunity.enums.ContentType;
import com.example.SmartCommunity.enums.SenderType;
import com.example.SmartCommunity.model.ChatMessage;
import com.example.SmartCommunity.model.ChatSession;
import com.example.SmartCommunity.repository.ChatMessageRepository;
import com.example.SmartCommunity.repository.ChatSessionRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.AiAssistantService;
import com.example.SmartCommunity.service.ChatMessageService;
import com.example.SmartCommunity.util.FileTypeUtils;
import com.example.SmartCommunity.util.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    private final MultiModalConversation conv;
    private final ChatMessageService chatMessageService;
    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public AiAssistantServiceImpl(ChatMessageService chatMessageService, ChatSessionRepository chatSessionRepository, UserRepository userRepository, ChatMessageRepository chatMessageRepository) {
        this.chatMessageService = chatMessageService;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.conv = new MultiModalConversation();
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> processMultiModalInput(Long sessionId,Long userId,String text, MultipartFile file)
            throws NoApiKeyException, UploadFileException, ApiException, InputRequiredException {
        ChatSession chatSession;
        if(sessionId == null) {
            chatSession = new ChatSession();
            chatSession.setUser(userRepository.getUserById(userId));
            String prompt = "请为这段对话生成一个简洁的标题：" + text;
            chatSession.setSessionName(generateTitle(prompt));
            chatSession = chatSessionRepository.save(chatSession);
        }else {
            chatSession = chatSessionRepository.findById(sessionId).orElseThrow(() ->
                    new NoSuchElementException("不存在sessionId为" + sessionId + "的会话记录"));
        }

        chatMessageService.saveMessage(chatSession, SenderType.USER.getCode(), ContentType.TEXT.getCode(), text);

        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            String fileUrl  = uploadFile(file);
            if (FileTypeUtils.isImage(contentType)) {
                chatMessageService.saveMessage(chatSession, SenderType.USER.getCode(), ContentType.IMAGE.getCode(), fileUrl);
            } else if (FileTypeUtils.isVideo(contentType)) {
                chatMessageService.saveMessage(chatSession, SenderType.USER.getCode(), ContentType.VIDEO.getCode(), fileUrl);
            }else{
                throw new UploadFileException("只支持图片或视频类型文件，实际为：" + contentType);
            }
        }

        List<ChatMessage> history = chatMessageRepository.findTop10BySessionOrderByCreatedTimeAsc(chatSession);
        List<MultiModalMessage> messages = new ArrayList<>();
        messages.add(MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                .content(List.of(Map.of("text", "You are a helpful assistant."))).build());

        for (ChatMessage msg : history) {
            List<Map<String, Object>> contents = new ArrayList<>();
            switch (msg.getContentType()) {
                case 1 -> contents.add(Map.of("text", msg.getContent()));
                case 2 -> contents.add(Map.of("image", msg.getContent()));
                case 3 -> contents.add(Map.of("video", msg.getContent()));
            }
            messages.add(MultiModalMessage.builder()
                    .role(msg.getSender() == SenderType.USER.getCode() ? Role.USER.getValue() : Role.ASSISTANT.getValue())
                    .content(contents).build());
        }

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("qwen-vl-plus")
                .messages(messages)
                .build();

        MultiModalConversationResult result = conv.call(param);
        String aiResponse = (String) result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");

        // 保存 AI 回复
        chatMessageService.saveMessage(chatSession, SenderType.AI.getCode(), ContentType.TEXT.getCode(), aiResponse);

        chatSession.setLastUpdatedTime(LocalDateTime.now());
        chatSessionRepository.save(chatSession);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", chatSession.getId());
        response.put("aiResponse", aiResponse);
        return response;
    }

    private String generateTitle(String prompt) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个智能助手，任务是根据用户的问题生成一个简洁的对话标题")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("qwen-plus")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult result = gen.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }

    private String uploadFile(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        String objectName = "AiChatFile/" + uniqueFileName;
        OSSUtils.uploadFileToOSS(file, objectName);
        return "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/" + objectName;
    }
}