package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ChatMessage;
import com.example.SmartCommunity.model.ChatSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10BySessionOrderByCreatedTimeAsc(@NotNull ChatSession session);
}
