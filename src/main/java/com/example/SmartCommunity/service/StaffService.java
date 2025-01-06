package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.StaffWithEvaluatedCount;
import com.example.SmartCommunity.dto.StaffWithServicesDTO;
import com.example.SmartCommunity.dto.UpdatedStaffInfoDTO;
import com.example.SmartCommunity.model.Staff;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StaffService {
    List<Staff> getAllStaff();

    // 根据人名和部门筛选评价人员
    List<Staff> searchStaff(String name, String department);

    StaffWithServicesDTO getStaffWithServices(Long staffId);

    List<StaffWithEvaluatedCount> getStaffEvaluationCounts();

    Map<String, Object> login(Long staffId, String phone, String password);

    void changePassword(String username, String phone, String newPassword);

    UpdatedStaffInfoDTO updateProfile(Long staffId, String name, String phoneNumber, MultipartFile avatar);

    String staffAvatar(Long id, MultipartFile file);

    Map<String, Object> getStaffById(Long staffId);
}
