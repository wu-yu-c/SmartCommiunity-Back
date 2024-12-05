package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal; // 导入 BigDecimal
import java.sql.Timestamp; // 导入 Timestamp
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Evaluator")
public class Evaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EvaluatorID")
    private Long evaluatorID;

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

    @Column(name = "CreatedTime", insertable = false, updatable = false)
    private Timestamp createdTime;

    // 外键关联 User 表
    @ManyToOne
    @JoinColumn(name = "EvaluatorID", referencedColumnName = "UserID", nullable = false) // 外键关联 UserID
    private User user;

    // 外键关联 ManagementArea 表
    @ManyToOne
    @JoinColumn(name = "AreaID", referencedColumnName = "AreaID", nullable = true) // 外键关联 AreaID
    private ManagementArea managementArea;

}
