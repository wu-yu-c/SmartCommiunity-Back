package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairIssueRepository extends JpaRepository<RepairIssue, Integer> {
    Optional<RepairIssue> findRepairIssueById(Long id);

    List<RepairIssue> findAllByReporter(User reporter);
}