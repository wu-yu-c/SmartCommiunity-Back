package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.ChatMessage;
import com.example.SmartCommunity.model.ChatSession;
import com.example.SmartCommunity.repository.ChatMessageRepository;
import com.example.SmartCommunity.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
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
}
