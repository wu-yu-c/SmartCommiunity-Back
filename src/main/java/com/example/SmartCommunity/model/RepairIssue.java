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
@Table(name = "repairissue", schema = "smart_community")
public class RepairIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自动生成主键值
    @Column(name = "RepairIssueID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "ResidentID", nullable = false)
    private Long residentID;

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

    @Size(max = 256)
    @Column(name = "VideoUrl", length = 256)
    private String videoUrl;

    @Size(max = 256)
    @Column(name = "ImageUrl", length = 256)
    private String imageUrl;

}