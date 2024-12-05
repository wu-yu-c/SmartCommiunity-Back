package com.example.SmartCommunity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Resident", schema = "testdb")
public class Resident {

    @Id
    @Column(name = "ResidentID", nullable = false)
    private Long residentID; // 将 Integer 改为 Long，匹配数据库中的 bigint

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

    // 外键关联 User 表
    @ManyToOne
    @JoinColumn(name = "ResidentID", referencedColumnName = "UserID", nullable = false) // 外键关联 UserID
    private User user;

    // 外键关联 ManagementArea 表
    @ManyToOne
    @JoinColumn(name = "AreaID", referencedColumnName = "AreaID", nullable = true) // 外键关联 AreaID
    private ManagementArea managementArea;
}
