package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "repair_issue", schema = "SmartCommunity")
public class RepairIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reporter_id", nullable = false)
    private com.example.SmartCommunity.model.User reporter;

    @Size(max = 100)
    @NotNull
    @Column(name = "issue_title", nullable = false, length = 100)
    private String issueTitle;

    @NotNull
    @Lob
    @Column(name = "issue_description", nullable = false)
    private String issueDescription;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id", nullable = false)
    private IssueCategory category;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @NotNull
    @ColumnDefault("'pending'")
    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 255)
    @Column(name = "video_url")
    private String videoUrl;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "assigned_id")
    private com.example.SmartCommunity.model.Staff assigned;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @Lob
    @Column(name = "completion_note")
    private String completionNote;

    @PrePersist
    protected void onCreated(){
        this.createdTime = LocalDateTime.now();
        this.status = "pending";
    }
}