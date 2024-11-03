package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "RepairIssue")
public class RepairIssue {
    @Id
    @Column(name = "IssueID", nullable = false)
    private Integer id;

    @NotNull
    @Lob
    @Column(name = "Description", nullable = false)
    private String description;

    @NotNull
    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "CreatedAt", nullable = false)
    private Instant createdAt;

}