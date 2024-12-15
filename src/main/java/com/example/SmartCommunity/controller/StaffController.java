package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.StaffService;
import com.example.SmartCommunity.dto.StaffResponseDTO;
import com.example.SmartCommunity.model.Staff;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Tag(name="工作人员相关接口")
@RestController
@RequestMapping("/Staff")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(summary = "获取工作人员列表",description = "返回工作人员列表，属性包括id、姓名、位置、头像、部门" +
            "、平均分以及负责的区域编号")
    @GetMapping("/getStaffList")
    public ResponseEntity<List<StaffResponseDTO>> getStaffList() {
        try {
            List<Staff> staff = staffService.getAllStaff();
            List<StaffResponseDTO> responseDTOs = staff.stream()
                    .map(StaffResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "根据人名和部门筛选评价人员",description = "从输入的人名和部门筛选符合条件的工作人员，返回人员列表")
    @GetMapping("/searchStaff")
    public ResponseEntity<Map<String, Object>> searchEvaluators(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {

        try {
            List<Staff> staff = staffService.searchStaff(name, department);
            List<StaffResponseDTO> responseDTOs = staff.stream()
                    .map(StaffResponseDTO::new)
                    .toList();

            // 返回的响应包含查询结果数量和结果列表
            return ResponseEntity.ok(Map.of(
                    "count", staff.size(),
                    "staff", responseDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询出错，请稍后再试"));
        }
    }

    // 异常处理方法
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "内部服务器错误", "message", ex.getMessage()));
    }
}
