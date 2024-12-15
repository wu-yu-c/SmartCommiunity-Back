package com.example.SmartCommunity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "ChatTopic")
public class ChatTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TopicID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnore
    private User userID;

    @Size(max = 255)
    @NotNull
    @Column(name = "TopicName", nullable = false)
    private String topicName;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreateTime")
    private Instant createTime;

    @PrePersist
    public void prePersist() {
        if (this.createTime == null) {
            this.createTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).toInstant();
        }
    }
}