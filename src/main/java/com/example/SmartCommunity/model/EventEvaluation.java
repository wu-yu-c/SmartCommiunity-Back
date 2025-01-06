package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "EventEvaluation")
public class EventEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EventID")
    private Long EventID; // 主键：事件ID，类型为 int

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "EvaluatorID", referencedColumnName = "UserID")
    private User user; // 外键：EvaluatorID，引用 User 表的 UserID

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "WorkerID", referencedColumnName = "StaffID")
    private Staff staff; // 外键：WorkerID，引用 Staff 表的 StaffID

    @Setter
    @Column(name = "Description", columnDefinition = "TEXT", nullable = false)
    private String Description; // 事件描述，类型为 TEXT

    @Setter
    @Column(name = "Score", nullable = false)
    private Integer Score; // 评分，类型为 int，限制为 1-5

    @Setter
    @Column(name = "Content", columnDefinition = "TEXT", nullable = false)
    private String Content; // 评价内容，类型为 TEXT

    @Getter
    @Column(name = "CreatedTime", nullable = false)
    private Timestamp CreatedTime; // 创建时间，类型为 DATETIME

    public void setCreatedTime(LocalDateTime now) {
        this.CreatedTime = Timestamp.valueOf(now);
    }
}
