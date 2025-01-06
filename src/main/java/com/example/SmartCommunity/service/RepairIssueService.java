package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponseDTO;
import com.example.SmartCommunity.model.RepairIssue;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepairIssueService {
    // 实现原有的CRUD方法
    List<RepairIssue> findAll();

    RepairIssue findById(Integer id);

    RepairIssue update(RepairIssue repairIssue);

    void deleteById(Integer id);

    RepairIssue createRepairIssue(RepairIssueDTO dto, MultipartFile imageFile, MultipartFile videoFile);

    RepairIssueResponseDTO getRepairIssueInfo(Integer id);

    List<RepairIssueResponseDTO> getRepairIssueByResidentId(Long residentId);

    List<String> getAllRepairAddresses();
}