package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Staff;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    // 根据人名（模糊查询）和所属部门（精确查询）筛选评价人员
    List<Staff> findByNameContainingAndDepartmentContaining(String name, String department);

    Staff findStaffByStaffID(Long staffID);

    @Query("SELECT s FROM Staff s WHERE s.phoneNumber = :phoneNumber")
    Staff findStaffByPhoneNumber(@Size(max = 255) String phoneNumber);

    @Query("SELECT s FROM Staff s WHERE s.name = :name AND s.phoneNumber = :phoneNumber")
    Optional<Staff> findbyNameAndPhoneNumber(@Size(max = 255) String name, @Size(max = 255) String phoneNumber);
}
