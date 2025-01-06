package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.*;
import com.example.SmartCommunity.service.StaffService;
import com.example.SmartCommunity.model.Staff;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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

    @Operation(summary = "员工登录接口")
    @PostMapping("/staffLogin")
    public ResponseEntity<Map<String,Object>> staffLogin(@RequestParam(required = false) Long staffId,
                                                         @RequestParam(required = false) String phone,
                                                         @RequestParam String password){
        Map<String,Object> map = new HashMap<>();
        try{
            // 调用服务层进行职工登录，传入 name 或 phone 和密码
            Map<String, Object> result = staffService.login(staffId, phone, password);
            return ResponseEntity.status((Integer) result.get("code")).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(summary="通过id获取员工详情")
    @GetMapping("/getStaffById")
    public ResponseEntity<Map<String,Object>> getStaffById(@RequestParam Long staffId){
        try{
            Map<String,Object>result =staffService.getStaffById(staffId);
            return ResponseEntity.status((Integer) result.get("code")).body(result);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new HashMap<>(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "获取工作人员列表",description = "返回工作人员列表，属性包括id、姓名、职位、头像、部门" +
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

    @Operation(summary = "根据人名和部门筛选工作人员",description = "从输入的人名和部门筛选符合条件的工作人员，返回人员列表")
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

    @Operation(summary = "修改职工密码接口")
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String name,
                                                 @RequestParam String phone,
                                                 @RequestParam String newPassword) {
        try {
            staffService.changePassword(name, phone, newPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "返回每个工作人员被评分的次数",description = "返回一个人员列表，除了职工基本信息，还包括其被评价次数")
    @GetMapping("/getStaffWithEvaluatedCount")
    public ResponseEntity<?> getStaffWithEvaluatedCount() {
        try {
            // 调用 service 层方法来获取工作人员的基本信息及评价次数
            List<StaffWithEvaluatedCount> staffEvaluationCounts = staffService.getStaffEvaluationCounts();
            if (staffEvaluationCounts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("没有找到工作人员的评价记录.");
            }
            return ResponseEntity.ok(staffEvaluationCounts);

        } catch (Exception ex) {
            // 处理其他未知异常的情况，返回 500 错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器发生错误，请稍后重试.");
        }
    }

    @Operation(summary = "修改职工信息接口", description = "返回修改成功与否的结果")
    @PostMapping(value="/updateStaffInfo",consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> updateStaffInfo(
            @RequestParam Long staffId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        Map<String, Object> response = new HashMap<>();
        try {
            UpdatedStaffInfoDTO updatedStaff = staffService.updateProfile(staffId, name, phoneNumber, avatar);
            response.put("code", HttpStatus.OK.value());
            response.put("message", "Profile updated successfully");
            response.put("data", updatedStaff);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error occurred while updating profile");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 异常处理方法
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "内部服务器错误", "message", ex.getMessage()));
    }
}
