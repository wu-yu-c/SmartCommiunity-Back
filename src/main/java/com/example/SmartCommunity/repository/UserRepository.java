package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUserPhone(String userPhone);

    Optional<Object> findByUsernameAndUserPhone(String username, String userPhone);
}