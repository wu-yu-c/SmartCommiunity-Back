package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.ChatMessageDTO;
import com.example.SmartCommunity.model.ChatMessage;
import com.example.SmartCommunity.model.ChatSession;
import com.example.SmartCommunity.repository.ChatMessageRepository;
import com.example.SmartCommunity.repository.ChatSessionRepository;
import com.example.SmartCommunity.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatSessionRepository chatSessionRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    @Transactional
    public void saveMessage(ChatSession session, Byte sender, Byte contentType, String content) {
        ChatMessage message = new ChatMessage();
        message.setSession(session);
        message.setSender(sender);
        message.setContentType(contentType);
        message.setContent(content);
        chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessageDTO> getMessagesBySessionId(Long sessionId, Long userId){
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("会话不存在"));
        if (!session.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("您没有权限访问该会话");
        }

        List<ChatMessage> messages = chatMessageRepository.findBySession_IdOrderByCreatedTimeAsc(sessionId);
        return messages.stream().map(ChatMessageDTO::new).toList();
    }
}
