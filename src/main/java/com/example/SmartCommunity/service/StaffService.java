package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.StaffInfoDTO;
import com.example.SmartCommunity.dto.StaffWithReviewsDTO;
import com.example.SmartCommunity.model.Staff;

import java.util.List;

public interface StaffService {
    StaffInfoDTO getStaffById(Long staffId);

    List<Staff> getAllStaff();

    /**
     * 根据人名和部门筛选符合条件的工作人员
     */
    List<Staff> searchStaff(String staffName, String department);

    StaffWithReviewsDTO getStaffWithServices();
}
