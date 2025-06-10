package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.SubmitRepairResultRequest;
import com.example.SmartCommunity.dto.UploadRepairIssueRequest;
import com.example.SmartCommunity.model.RepairIssue;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepairIssueService {
    List<RepairIssue> findAll();

    void uploadRepairIssue(UploadRepairIssueRequest request, MultipartFile imageFile, MultipartFile videoFile);

    RepairIssueDTO getRepairIssueInfo(Long issueId);

    List<RepairIssue> getRepairIssuesByUserId();

    void submitRepairResult(SubmitRepairResultRequest request);

    void deleteRepairById(Long id);

    List<RepairIssueDTO> getTasksForCurrentStaff();
}