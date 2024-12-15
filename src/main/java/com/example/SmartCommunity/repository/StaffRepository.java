package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    // 根据人名（模糊查询）和所属部门（精确查询）筛选评价人员
    List<Staff> findByNameContainingAndDepartmentContaining(String name, String department);
}
