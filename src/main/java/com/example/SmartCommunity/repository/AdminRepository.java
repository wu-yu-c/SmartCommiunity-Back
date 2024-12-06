package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAdminName(String adminName);

    Admin findByAdminPhone(String adminPhone); // 根据电话查找管理员
}
