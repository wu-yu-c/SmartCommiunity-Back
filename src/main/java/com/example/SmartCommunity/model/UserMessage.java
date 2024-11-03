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
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ResidentID")
    private Resident residentID;

    @NotNull
    @Lob
    @Column(name = "MessageContent", nullable = false)
    private String messageContent;

    @NotNull
    @Column(name = "SentAt", nullable = false)
    private Instant sentAt;

}