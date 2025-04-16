package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chat_session", schema = "SmartCommunity")
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255)
    @Column(name = "session_name")
    private String sessionName;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "session")
    private Set<ChatMessage> chatMessages = new LinkedHashSet<>();

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}