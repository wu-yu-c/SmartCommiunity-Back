package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.UploadRepairIssueRequest;
import com.example.SmartCommunity.model.IssueCategory;
import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.IssueCategoryRepository;
import com.example.SmartCommunity.repository.RepairIssueRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.RepairIssueService;
import com.example.SmartCommunity.util.OSSUtils;
import jdk.jfr.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class RepairIssueServiceImpl implements RepairIssueService {

    private final RepairIssueRepository repairIssueRepository;
    private final UserRepository userRepository;
    private final IssueCategoryRepository issueCategoryRepository;

    @Autowired
    public RepairIssueServiceImpl(RepairIssueRepository repairIssueRepository, UserRepository userRepository,
                                  IssueCategoryRepository issueCategoryRepository) {
        this.repairIssueRepository = repairIssueRepository;
        this.userRepository = userRepository;
        this.issueCategoryRepository = issueCategoryRepository;
    }

    @Override
    public List<RepairIssue> findAll() {
        return repairIssueRepository.findAll();
    }
//
//    @Override
//    public RepairIssue findById(Integer id) {
//        return repairIssueRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Repair issue not found"));
//    }
//
//    @Override
//    public RepairIssue update(RepairIssue repairIssue) {
//        if (!repairIssueRepository.existsById(Math.toIntExact(repairIssue.getId()))) {
//            throw new RuntimeException("Repair issue not found");
//        }
//        return repairIssueRepository.save(repairIssue);
//    }
//
//    @Override
//    public void deleteById(Integer id) {
//        repairIssueRepository.deleteById(id);
//    }
//
    @Override
    public void uploadRepairIssue(UploadRepairIssueRequest request, MultipartFile imageFile, MultipartFile videoFile) {
        RepairIssue repairIssue = new RepairIssue();
        // 复制 request 的属性到 repairIssue
        BeanUtils.copyProperties(request, repairIssue);

        // 处理 reporter 字段（从 userId 查找 User）
        User user = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new NoSuchElementException("用户不存在"));
        repairIssue.setReporter(user);

        IssueCategory category = issueCategoryRepository.findById(request.getCategoryId()).
                orElseThrow(()->new NoSuchElementException("不存在类别Id为"+request.getCategoryId()+"的报修问题"));
        repairIssue.setCategory(category);

        if (imageFile != null)
            uploadFile(imageFile, repairIssue, 0);
        if (videoFile != null)
            uploadFile(videoFile, repairIssue, 1);
        repairIssueRepository.save(repairIssue);
    }

    public void uploadFile(MultipartFile imageFile, RepairIssue repairIssue, Integer type) {
        try {
            String originalFileName = imageFile.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID() + fileExtension;
            String objectName = "IssueImageAndVideo/" + uniqueFileName;
            String result = OSSUtils.uploadFileToOSS(imageFile, objectName);
            if (result.equals("failure")) {
                throw new RuntimeException("上传文件失败");
            }
            String fileUrl = "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/" + objectName;
            if (type.equals(0))
                repairIssue.setImageUrl(fileUrl);
            else
                repairIssue.setVideoUrl(fileUrl);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败");
        }
    }

    @Override
    public RepairIssueDTO getRepairIssueInfo(Long issueId) {
        RepairIssue repairIssue = repairIssueRepository.findRepairIssueById(issueId)
                .orElseThrow(()->new NoSuchElementException("该报修事件不存在"));

        return new RepairIssueDTO(repairIssue);
    }

    @Override
    public List<RepairIssue> getRepairIssuesByUserId(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null)
            throw new NoSuchElementException("未找到该用户");

        return repairIssueRepository.findAllByReporter(user);
    }
}