package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ChatTopic;
import com.example.SmartCommunity.model.UserMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    Page<UserMessage> findByTopicID(ChatTopic topicID, Pageable pageable);
    List<UserMessage> findByTopicID(ChatTopic chatTopic);
}
