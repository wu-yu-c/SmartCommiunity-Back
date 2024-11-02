package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "IssueCategory")
public class IssueCategory {
    @Id
    @Column(name = "CategoryID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "CategoryName", nullable = false)
    private String categoryName;

}