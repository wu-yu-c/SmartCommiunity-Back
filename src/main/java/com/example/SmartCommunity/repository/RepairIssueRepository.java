package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.RepairIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairIssueRepository extends JpaRepository<RepairIssue, Integer> {
    List<RepairIssue> findAllByResidentID(Long residentId);

    @Query("SELECT r.repairAddress FROM RepairIssue r")
    List<String> findAllRepairAddress();
}