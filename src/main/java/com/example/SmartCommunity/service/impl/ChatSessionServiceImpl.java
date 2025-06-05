package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.enums.ContentType;
import com.example.SmartCommunity.model.ChatMessage;
import com.example.SmartCommunity.model.ChatSession;
import com.example.SmartCommunity.repository.ChatMessageRepository;
import com.example.SmartCommunity.repository.ChatSessionRepository;
import com.example.SmartCommunity.service.ChatSessionService;
import com.example.SmartCommunity.util.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatSessionServiceImpl(ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public void deleteSessionById(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                        .orElseThrow(()->new NoSuchElementException("未找到sessionId为" + sessionId+"的会话"));
        List<ChatMessage> messages = chatMessageRepository.findBySessionAndContentTypeIn(
                session, List.of(ContentType.IMAGE.getCode(), ContentType.VIDEO.getCode()));
        for (ChatMessage message : messages) {
            String fileUrl = message.getContent();
            fileUrl = fileUrl.replace("https://1st-bucket.oss-cn-shanghai.aliyuncs.com/", "");
            OSSUtils.deleteFile(fileUrl);
        }
        chatSessionRepository.delete(session);
    }
}
