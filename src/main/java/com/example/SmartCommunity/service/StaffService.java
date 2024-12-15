package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.Staff;
import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();

    // 根据人名和部门筛选评价人员
    List<Staff> searchStaff(String name, String department);
}
