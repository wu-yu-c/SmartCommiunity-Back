package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdminID")
    private Long adminID;

    @Column(name = "AdminName", nullable = false)
    private String adminName;

    @Column(name = "AdminAddress")
    private String adminAddress;

    @Column(name = "AdminPhone")
    private String adminPhone;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "CreatedTime", insertable = false, updatable = false)
    private Timestamp createdTime;

    // 多对多关系：管理员与管理区域的多对多关系
    @ManyToMany
    @JoinTable(name = "Admin_ManagementArea", // 连接表名称
            joinColumns = @JoinColumn(name = "AdminID"), // 当前实体（Admin）对应的列
            inverseJoinColumns = @JoinColumn(name = "AreaID") // 关联实体（ManagementArea）对应的列
    )
    private Set<ManagementArea> managementAreas; // 管理员对应的管理区域

}
