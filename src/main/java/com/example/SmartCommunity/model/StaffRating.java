package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "staff_rating", schema = "SmartCommunity", indexes = {
        @Index(name = "rater_idx", columnList = "rater_id"),
        @Index(name = "staff_idx", columnList = "rated_staff_id")
})
public class StaffRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rater_id", nullable = false)
    private User rater;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rated_staff_id", nullable = false, referencedColumnName = "staff_id")
    private Staff ratedStaff;

    @NotNull
    @Min(value = 1, message = "评分必须是1-5之间的整数")
    @Max(value = 5, message = "评分必须是1-5之间的整数")
    @Column(name = "score", nullable = false)
    private Integer score;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "rating_time", nullable = false)
    private LocalDateTime ratingTime;

    @PrePersist
    protected void onCreate() {
        ratingTime = LocalDateTime.now();
    }
}