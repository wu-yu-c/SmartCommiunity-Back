package com.example.SmartCommunity.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.SubmitRepairResultRequest;
import com.example.SmartCommunity.dto.UploadRepairIssueRequest;
import com.example.SmartCommunity.enums.RepairStatusType;
import com.example.SmartCommunity.model.IssueCategory;
import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.IssueCategoryRepository;
import com.example.SmartCommunity.repository.RepairIssueRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.RepairIssueService;
import com.example.SmartCommunity.util.OSSUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RepairIssueServiceImpl implements RepairIssueService {

    private final RepairIssueRepository repairIssueRepository;
    private final UserRepository userRepository;
    private final IssueCategoryRepository issueCategoryRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public RepairIssueServiceImpl(RepairIssueRepository repairIssueRepository, UserRepository userRepository,
                                  IssueCategoryRepository issueCategoryRepository, StaffRepository staffRepository) {
        this.repairIssueRepository = repairIssueRepository;
        this.userRepository = userRepository;
        this.issueCategoryRepository = issueCategoryRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public List<RepairIssue> findAll() {
        return repairIssueRepository.findAll();
    }

    @Override
    public void uploadRepairIssue(UploadRepairIssueRequest request, MultipartFile imageFile, MultipartFile videoFile) {
        RepairIssue repairIssue = new RepairIssue();
        // 复制 request 的属性到 repairIssue
        BeanUtils.copyProperties(request, repairIssue);

        // 处理 reporter 字段（从 userId 查找 User）
        User user = userRepository.findById(StpUtil.getLoginIdAsLong())
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

        assignRepairIssueToStaff(repairIssue);
    }

    public void uploadFile(MultipartFile imageFile, RepairIssue repairIssue, Integer type) {
        String originalFileName = imageFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + fileExtension;
        String objectName = "IssueImageAndVideo/" + uniqueFileName;
        OSSUtils.uploadFileToOSS(imageFile, objectName);
        String fileUrl = "https://1st-bucket.oss-cn-shanghai.aliyuncs.com/" + objectName;
        if (type.equals(0))
            repairIssue.setImageUrl(fileUrl);
        else
            repairIssue.setVideoUrl(fileUrl);
    }

    @Override
    public RepairIssueDTO getRepairIssueInfo(Long issueId) {
        RepairIssue repairIssue = repairIssueRepository.findRepairIssueById(issueId)
                .orElseThrow(()->new NoSuchElementException("该报修事件不存在"));

        return new RepairIssueDTO(repairIssue);
    }

    @Override
    public void deleteRepairById(Long id){
        if(!repairIssueRepository.existsById(id))
            throw new NoSuchElementException("该报修事件不存在");
        repairIssueRepository.deleteById(id);
    }

    @Override
    public List<RepairIssue> getRepairIssuesByUserId() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userRepository.findUserById(userId);
        if (user == null)
            throw new NoSuchElementException("未找到该用户");

        return repairIssueRepository.findAllByReporter(user);
    }

    @Override
    @Transactional
    public void submitRepairResult(SubmitRepairResultRequest request){
        Long userId = StpUtil.getLoginIdAsLong();
        Staff staff = staffRepository.findById(userId).
                orElseThrow(()->new AccessDeniedException("没有权限：您不是员工！"));
        RepairIssue issue = repairIssueRepository.findRepairIssueById(request.getIssueId())
                .orElseThrow(() -> new NoSuchElementException("报修事件不存在"));
        if (issue.getAssigned() == null || !issue.getAssigned().getStaffId().equals(staff.getStaffId())) {
            throw new AccessDeniedException("你无权限处理该事件");
        }
        issue.setStatus(request.getStatus());
        issue.setCompletedTime(LocalDateTime.now());
        if(!request.getCompletionNote().isBlank())
            issue.setCompletionNote(request.getCompletionNote());
        repairIssueRepository.save(issue);
    }

    @Override
    public List<RepairIssueDTO> getTasksForCurrentStaff(){
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("用户不存在"));

        Staff staff = staffRepository.findByUser(user);
        if (staff == null) {
            throw new IllegalStateException("该用户不是工作人员");
        }

        List<RepairIssue> tasks = repairIssueRepository.findByAssigned(staff);
        tasks.sort(Comparator
                .comparingInt((RepairIssue issue) -> getStatusPriority(issue.getStatus()))
                .thenComparing(RepairIssue::getCreatedTime, Comparator.reverseOrder())
        );
        return tasks.stream().map(RepairIssueDTO::new).toList();
    }

    // 定义一个状态优先级方法：数值越小优先级越高
    private int getStatusPriority(RepairStatusType status) {
        return switch (status) {
            case PENDING -> 0;
            case IN_PROGRESS -> 1;
            default -> 2; // 其他状态排最后
        };
    }

    private void assignRepairIssueToStaff(RepairIssue issue) {
        String department = issue.getCategory().getDepartment();
        List<Staff> staffList = staffRepository.findByDepartment(department);

        if (staffList.isEmpty()) {
            throw new NoSuchElementException("该部门暂无员工");
        }

        Collection<String> targetStatuses = List.of(RepairStatusType.COMPLETED.name(), RepairStatusType.REJECTED.name());
        Staff assigned = staffList.stream()
                .min(Comparator.comparing(staff ->
                        repairIssueRepository.countByAssignedAndStatusNotIn(staff,targetStatuses)))
                .orElseThrow(() -> new RuntimeException("目前无法分配任务"));

        issue.setAssigned(assigned);
        repairIssueRepository.save(issue);
    }
}