package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUserName(String username);

    boolean existsUserByPhoneNumber(String phoneNumber);

    User findUserByUserNameOrPhoneNumber(String userName, String phoneNumber);

    Optional<User> findUserByUserNameAndPhoneNumber(String userName, String phoneNumber);

    User findUserById(Long userId);

    @NotNull User getUserById(Long id);
}