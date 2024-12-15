package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.service.StaffService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public List<Staff> searchStaff(String name, String department) {
        // 如果人名和部门都为空，返回所有评价人员
        if (name == null && department == null) {
            return getAllStaff();
        }

        // 调用仓库方法，根据传入的条件进行筛选
        return staffRepository.findByNameContainingAndDepartmentContaining(name != null ? name : "",
                department != null ? department : "");
    }
}
