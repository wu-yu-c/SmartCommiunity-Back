package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {

}
