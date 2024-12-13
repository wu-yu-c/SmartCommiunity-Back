package com.example.SmartCommunity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Entity
@Table(name = "AiMessage")
public class AiMessage implements Comparable<AiMessage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageID", nullable = false)
    private Long id;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserMessage", nullable = false)
    private UserMessage userMessage;

    @Lob
    @Column(name = "ContentText")
    private String contentText;

    @Override
    public int compareTo(AiMessage o) {
        return this.id.compareTo(o.id);
    }

}