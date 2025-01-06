package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Repairissue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairIssueRepository extends JpaRepository<Repairissue, Integer> {
    List<Repairissue> findAllByResidentID(Long residentId);

    @Query("SELECT r.repairAddress FROM Repairissue r")
    List<String> findAllRepairAddress();
}