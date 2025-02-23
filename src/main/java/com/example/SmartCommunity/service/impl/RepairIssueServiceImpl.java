package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponseDTO;
import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.repository.RepairIssueRepository;
import com.example.SmartCommunity.service.RepairIssueService;
import com.example.SmartCommunity.util.OSSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RepairIssueServiceImpl implements RepairIssueService {

    @Autowired
    private RepairIssueRepository repairIssueRepository;

    // 实现原有的CRUD方法
    @Override
    public List<RepairIssue> findAll() {
        return repairIssueRepository.findAll();
    }

    @Override
    public RepairIssue findById(Integer id) {
        return repairIssueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repair issue not found"));
    }

    @Override
    public RepairIssue update(RepairIssue repairIssue) {
        if (!repairIssueRepository.existsById(Math.toIntExact(repairIssue.getId()))) {
            throw new RuntimeException("Repair issue not found");
        }
        return repairIssueRepository.save(repairIssue);
    }

    @Override
    public void deleteById(Integer id) {
        repairIssueRepository.deleteById(id);
    }

    @Override
    public RepairIssue createRepairIssue(RepairIssueDTO dto, MultipartFile imageFile, MultipartFile videoFile) {
        try {
            RepairIssue repairIssue = new RepairIssue();
            BeanUtils.copyProperties(dto, repairIssue);
            if (imageFile != null)
                upload(imageFile, repairIssue,0);
            if (videoFile != null)
                upload(videoFile, repairIssue,1);
            // 设置默认字段值
            repairIssue.setRepairIssueStart(Instant.now());
            // 保存到数据库
            return repairIssueRepository.save(repairIssue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create repair issue: " + e.getMessage(), e);
        }
    }

    public void upload(MultipartFile imageFile,RepairIssue repairIssue,Integer type) {
        try {
            String originalFileName = imageFile.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;
            String objectName = "RepairIssueImageOrVideo/" + uniqueFileName;
            String result = OSSUtils.uploadFileToOSS(imageFile, objectName);
            String fileUrl = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/" + objectName;
            if (type.equals(0))
                repairIssue.setImageUrl(fileUrl);
            else
                repairIssue.setVideoUrl(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create repair issue: " + e.getMessage(), e);
        }
    }

    @Override
    public RepairIssueResponseDTO getRepairIssueInfo(Integer id) {
        RepairIssue repairIssue = findById(id);

        RepairIssueResponseDTO response = new RepairIssueResponseDTO();
        BeanUtils.copyProperties(repairIssue, response);
        return response;
    }

    @Override
    public List<RepairIssueResponseDTO> getRepairIssueByResidentId(Long residentId){
        List<RepairIssue> repairIssues = repairIssueRepository.findAllByResidentID(residentId);
        List<RepairIssueResponseDTO> responses = new ArrayList<>();
        for (RepairIssue issue : repairIssues) {
            RepairIssueResponseDTO response = new RepairIssueResponseDTO();
            // 复制属性
            BeanUtils.copyProperties(issue, response);
            responses.add(response);
        }
        return responses;
    }

    public List<String> getAllRepairAddresses() {
        return repairIssueRepository.findAllRepairAddress();
    }
}