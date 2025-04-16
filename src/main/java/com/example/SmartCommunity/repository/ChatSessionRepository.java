package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
}
