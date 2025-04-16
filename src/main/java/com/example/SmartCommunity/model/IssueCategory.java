package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "issue_category", schema = "SmartCommunity")
public class IssueCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Size(max = 50)
    @Column(name = "department", length = 50)
    private String department;

}