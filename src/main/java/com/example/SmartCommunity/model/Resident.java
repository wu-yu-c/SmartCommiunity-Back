package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Resident", schema = "smart_community")
public class Resident {

    @Id
    @Column(name = "ResidentID")
    private Long id; // 主键，同时作为外键

    @OneToOne
    @MapsId // 指定 ResidentID 是主键和外键
    @JoinColumn(name = "ResidentID", referencedColumnName = "UserID") // 外键关联 User 表的 UserID
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "ResidentName", nullable = false)
    private String residentName;

    @Size(max = 255)
    @NotNull
    @Column(name = "ResidentAddress", nullable = false)
    private String residentAddress;

    @Size(max = 50)
    @Column(name = "ResidentPhoneNumber", length = 50)
    private String residentPhoneNumber;

    // 外键关联 ManagementArea 表
    @ManyToOne
    @JoinColumn(name = "AreaID", referencedColumnName = "AreaID", nullable = true) // 外键关联 ManagementArea 的 AreaID
    private ManagementArea managementArea;
}
