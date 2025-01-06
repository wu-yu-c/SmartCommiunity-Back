package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "staff", schema = "smart_community")
public class Staff {
    @Id
    @Column(name = "StaffID", nullable = false)
    private Long staffID;

    @Size(max = 100)
    @NotNull
    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(max = 100)
    @Column(name = "Position", length = 100)
    private String position;

    @Size(max = 255)
    @Column(name = "Avatar")
    private String avatar;

    @Size(max = 100)
    @NotNull
    @Column(name = "Department", nullable = false, length = 100)
    private String department;

    @ColumnDefault("0.00")
    @Column(name = "AverageRating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Size(max = 255)
    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "AreaID", referencedColumnName = "AreaID", nullable = true)
    private ManagementArea managementArea;
}