package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByStaffId(Long staffId);

    List<Staff> findByDepartmentContainingAndUser_UserNameContaining(String department, String staffName);

    Staff findStaffByStaffId(@NotNull Long staffId);

    List<Staff> findByDepartment(@Size(max = 50) String department);

    Staff findByUser(User user);
}
