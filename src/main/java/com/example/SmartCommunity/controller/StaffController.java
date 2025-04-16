package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.dto.*;
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
import java.util.stream.Collectors;
import java.util.Map;

@Validated
@Tag(name="工作人员接口")
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(summary="通过工号获取职工信息")
    @GetMapping("/{staffId}")
    public ResponseEntity<ApiResponse<StaffInfoDTO>> getStaffById(@PathVariable @NotNull(message = "参数不能为空") Long staffId) {
        StaffInfoDTO response = staffService.getStaffById(staffId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "获取职工信息成功", response));
    }

    @JsonView(StaffInfoDTO.BasicView.class)
    @Operation(summary = "获取工作人员列表",description = "返回工作人员列表，属性包括id、姓名、职位、头像、部门、平均分")
    @GetMapping("/staffList")
    public ResponseEntity<ApiResponse<List<StaffInfoDTO>>> getStaffList() {
        List<Staff> staff = staffService.getAllStaff();
        List<StaffInfoDTO> response = staff.stream()
                .map(StaffInfoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "职工列表获取成功", response));
    }

    @JsonView(StaffInfoDTO.BasicView.class)
    @Operation(summary = "根据人名和部门筛选工作人员",description = "从输入的人名和部门筛选符合条件的工作人员，返回人员列表")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchStaffList(
            @RequestParam(required = false) String staffName,
            @RequestParam(required = false) String department) {
        List<Staff> staff = staffService.searchStaff(staffName, department);
        List<StaffInfoDTO> response = staff.stream()
                .map(StaffInfoDTO::new)
                .toList();

        Map<String, Object> result = Map.of("staffCount", response.size(),"staffList",response);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,"职工列表获取成功",result));
    }

    @Operation(summary = "根据工作人员id获取其基本信息和他收到的所有评分信息")
    @GetMapping("/{staffId}/rating-info")
    public ResponseEntity<ApiResponse<StaffWithReviewsDTO>> getEventEvaluationByStaffId(@PathVariable Long staffId) {
        StaffWithReviewsDTO response = staffService.getStaffWithServices(staffId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,"信息获取成功",response));
    }
}
