package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.repository.RepairIssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairIssueService {

    @Autowired
    private RepairIssueRepository repairIssueRepository;

    public List<RepairIssue> findAll() {
        return repairIssueRepository.findAll();
    }

    public RepairIssue findById(Integer id) {
        return repairIssueRepository.findById(id).orElse(null);
    }

    public RepairIssue save(RepairIssue repairIssue) {
        return repairIssueRepository.save(repairIssue);
    }

    public RepairIssue update(RepairIssue repairIssue) {
        return repairIssueRepository.save(repairIssue);
    }

    public void deleteById(Integer id) {
        repairIssueRepository.deleteById(id);
    }
}
