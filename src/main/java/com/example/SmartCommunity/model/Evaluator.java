package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    // 你可以添加其他的方法，如计算评分等
}
