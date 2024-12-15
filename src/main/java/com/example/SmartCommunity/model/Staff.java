package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID")
    private Long staffID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Position")
    private String position;

    @Column(name = "Avatar")
    private String avatar;

    @Column(name = "Department")
    private String department;

    @Column(name = "AverageRating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    // 外键关联 ManagementArea 表
    @ManyToOne
    @JoinColumn(name = "AreaID", referencedColumnName = "AreaID", nullable = true)
    private ManagementArea managementArea;
}
