package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Repairissue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairIssueRepository extends JpaRepository<Repairissue, Integer> {
}