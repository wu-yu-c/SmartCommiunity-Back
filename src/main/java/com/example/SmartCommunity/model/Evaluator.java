package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Evaluator")
public class Evaluator {

    @Id
    @Column(name = "EvaluatorID")
    private Long evaluatorID; // 主键，同时是外键

    @OneToOne
    @MapsId // 指定主键也是外键
    @JoinColumn(name = "EvaluatorID", referencedColumnName = "UserID") // 外键关联到 User 表的 UserID
    private User user;

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
