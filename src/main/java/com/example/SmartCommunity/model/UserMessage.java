package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "UserMessage")
public class UserMessage {

    @Id
    @Column(name = "MessageID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 外键关联 User 表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", nullable = true)
    private User user;

    @NotNull
    @Lob
    @Column(name = "MessageContent", nullable = false)
    private String messageContent;

    @NotNull
    @Column(name = "SentAt", nullable = false)
    private Instant sentAt;
}
