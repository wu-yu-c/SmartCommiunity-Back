package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.ServiceDTO;
import com.example.SmartCommunity.dto.StaffWithEvaluatedCount;
import com.example.SmartCommunity.dto.StaffWithServicesDTO;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.repository.EventEvaluationRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.service.StaffService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final EventEvaluationRepository eventEvaluationRepository;

    public StaffServiceImpl(StaffRepository staffRepository, EventEvaluationRepository eventEvaluationRepository) {
        this.staffRepository = staffRepository;
        this.eventEvaluationRepository = eventEvaluationRepository;
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

    public StaffWithServicesDTO getStaffWithServices(Long staffId) {
        // 获取工作人员信息
        Staff staff = staffRepository.findStaffByStaffID(staffId)
                .orElseThrow(() -> new NoSuchElementException("Staff not found with ID: " + staffId));

        // 获取工作人员的服务信息
        List<ServiceDTO> services =
                eventEvaluationRepository.findServicesByStaffId(staffId);

        // 封装为 DTO
        return new StaffWithServicesDTO(
                staff.getStaffID(),
                staff.getName(),
                staff.getAvatar(),
                staff.getPosition(),
                staff.getDepartment(),
                staff.getAverageRating(),
                services
        );
    }

    public List<StaffWithEvaluatedCount> getStaffEvaluationCounts() {
        try {
            List<StaffWithEvaluatedCount> staffEvaluationCounts = eventEvaluationRepository.findStaffEvaluationCounts();
            if (staffEvaluationCounts == null || staffEvaluationCounts.isEmpty()) {
                throw new Exception("没有找到工作人员的评价记录.");
            }
            return staffEvaluationCounts;
        } catch (Exception e) {
            // 记录日志，异常信息通过标准异常类抛出
            e.printStackTrace();  // 可以根据实际情况使用日志记录工具
            throw new RuntimeException("查询工作人员评价次数失败，请稍后重试.");
        }
    }
}
