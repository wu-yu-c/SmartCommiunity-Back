package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "staff", schema = "SmartCommunity", uniqueConstraints = {
        @UniqueConstraint(name = "staff_id_idx", columnNames = {"staff_id"})
})
public class Staff {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private com.example.SmartCommunity.model.User user;

    @NotNull
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Lob
    @Column(name = "job_description")
    private String jobDescription;

    @Size(max = 50)
    @Column(name = "department", length = 50)
    private String department;

    @Size(max = 50)
    @Column(name = "post", length = 50)
    private String post;

}