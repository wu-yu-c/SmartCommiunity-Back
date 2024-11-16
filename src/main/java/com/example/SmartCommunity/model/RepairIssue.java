package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "RepairIssue", schema = "testdb")
public class RepairIssue {
    @Id
    @Column(name = "RepairIssueID", nullable = false)
    private Integer id;

    @Column(name = "ResidentID")
    private Integer residentID;

    @NotNull
    @Lob
    @Column(name = "RepairIssueDetails", nullable = false)
    private String repairIssueDetails;

    @NotNull
    @Lob
    @Column(name = "RepairIssueStatus", nullable = false)
    private String repairIssueStatus;

    @NotNull
    @Column(name = "RepairIssueStart", nullable = false)
    private Instant repairIssueStart;

    @Size(max = 256)
    @NotNull
    @Column(name = "RepairAddress", nullable = false, length = 256)
    private String repairAddress;

    @Size(max = 256)
    @NotNull
    @Column(name = "RepairIssueTitle", nullable = false, length = 256)
    private String repairIssueTitle;

    @Size(max = 128)
    @NotNull
    @Column(name = "RepairIssueCategory", nullable = false, length = 128)
    private String repairIssueCategory;

}