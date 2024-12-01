package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.RepairIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairIssueRepository extends JpaRepository<RepairIssue, Integer> {
    // 其他自定义查询方法（如果需要）
}
