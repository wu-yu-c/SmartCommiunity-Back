package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUser_IdOrderByLastUpdatedTimeDesc(Long userId);
}
