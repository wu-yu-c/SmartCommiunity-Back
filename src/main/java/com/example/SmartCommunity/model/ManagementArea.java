package com.example.SmartCommunity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ManagementArea")
public class ManagementArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AreaID")
    private Long areaID;

    @Column(name = "AreaName", nullable = false, unique = true)
    private String areaName;

    // 多对多关系：管理区域与管理员的多对多关系
    @ManyToMany(mappedBy = "managementAreas") // 反向关系映射
    private Set<Admin> admins; // 该区域对应的所有管理员

}
