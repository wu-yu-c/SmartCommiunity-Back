package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.ManagementArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementAreaRepository extends JpaRepository<ManagementArea, Long> {
    ManagementArea findByAreaName(String areaName); // 根据区域名称查找区域
}
