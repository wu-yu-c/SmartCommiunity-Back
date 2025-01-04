package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponseDTO;
import com.example.SmartCommunity.model.Repairissue;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepairIssueService {
    // 实现原有的CRUD方法
    List<Repairissue> findAll();

    Repairissue findById(Integer id);

    Repairissue save(RepairIssueDTO repairIssue);

    Repairissue update(Repairissue repairIssue);

    void deleteById(Integer id);

    Repairissue createRepairIssueWithFiles(RepairIssueDTO dto, MultipartFile imageFile, MultipartFile videoFile);

    RepairIssueResponseDTO getRepairIssueWithFiles(Integer id);

    List<RepairIssueResponseDTO> getRepairIssueByResidentId(Long residentId);
}