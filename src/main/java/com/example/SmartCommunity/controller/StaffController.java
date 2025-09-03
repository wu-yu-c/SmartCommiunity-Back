package com.example.SmartCommunity.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.dto.*;
import com.example.SmartCommunity.service.RepairIssueService;
import com.example.SmartCommunity.service.StaffService;
import com.example.SmartCommunity.model.Staff;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Validated
@Tag(name="工作人员接口")
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    private final RepairIssueService repairIssueService;

    @Autowired
    public StaffController(StaffService staffService, RepairIssueService repairIssueService) {
        this.staffService = staffService;
        this.repairIssueService = repairIssueService;
    }

    @Operation(summary = "通过工号获取职工信息")
    @GetMapping("/{staffId}")
    public ResponseEntity<ApiResponse<StaffInfoDTO>> getStaffById(@PathVariable @NotNull(message = "参数不能为空") Long staffId) {
        StaffInfoDTO response = staffService.getStaffById(staffId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "获取职工信息成功", response));
    }

    @JsonView(StaffInfoDTO.RatingView.class)
    @Operation(summary = "获取工作人员列表", description = "返回工作人员列表，属性包括id、姓名、职位、头像、部门、平均分，以及当前用户对其评价")
    @GetMapping("/staffList")
    @SaCheckLogin
    public ResponseEntity<ApiResponse<List<StaffInfoDTO>>> getStaffList() {
        List<StaffInfoDTO> response = staffService.getAllStaff();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "职工列表获取成功", response));
    }

    @JsonView(StaffInfoDTO.BasicView.class)
    @Operation(summary = "根据人名和部门筛选工作人员", description = "从输入的人名和部门筛选符合条件的工作人员，返回人员列表")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchStaffList(
            @RequestParam(required = false) String staffName,
            @RequestParam(required = false) String department) {
        List<Staff> staff = staffService.searchStaff(staffName, department);
        List<StaffInfoDTO> response = staff.stream()
                .map(StaffInfoDTO::new)
                .toList();

        Map<String, Object> result = Map.of("staffCount", response.size(), "staffList", response);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "职工列表获取成功", result));
    }

    @Operation(summary = "根据工作人员id获取其基本信息和他收到的所有评分信息")
    @SaCheckLogin
    @GetMapping("/rating-info")
    public ResponseEntity<ApiResponse<StaffWithReviewsDTO>> getEventEvaluationByStaffId() {
        StaffWithReviewsDTO response = staffService.getStaffWithServices();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "信息获取成功", response));
    }

    @Operation(summary = "获取当前登录的工作人员的任务列表", description = "按照任务分配的时间由近到远排序，且优先展示未处理和处理中的报修")
    @SaCheckLogin
    @GetMapping("/assigned-issues")
    public ResponseEntity<ApiResponse<List<RepairIssueDTO>>> getMyRepairIssues() {
        List<RepairIssueDTO> tasks = repairIssueService.getTasksForCurrentStaff();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "任务列表获取成功", tasks));
    }
}
