package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ChatTopic;
import com.example.SmartCommunity.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatTopicRepository extends JpaRepository<ChatTopic, Long> {
    List<ChatTopic> findByUserID(@NotNull User user);
}
