package com.example.SmartCommunity.controller;

import com.alibaba.dashscope.app.Application;
import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.UploadRepairIssueRequest;
import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.service.RepairIssueService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Tag(name="报修接口")
@RestController
@RequestMapping("/api/repair-issues")
public class RepairIssueController {

    private final RepairIssueService repairIssueService;

    @Autowired
    public RepairIssueController(RepairIssueService repairIssueService) {
        this.repairIssueService = repairIssueService;
    }

    @JsonView(RepairIssueDTO.BasicView.class)
    @Operation(summary = "获取所有报修事件")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RepairIssueDTO>>> getAllRepairIssues() {
        List<RepairIssue> repairIssue = repairIssueService.findAll();
        List<RepairIssueDTO> response = repairIssue.stream()
                .map(RepairIssueDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "获取所有报修事件成功", response));
    }

    @Operation(summary = "通过事件ID获取报修事件的详情",description = "传入事件ID，返回该事件的详细信息")
    @GetMapping("/{issueId}")
    public ResponseEntity<ApiResponse<RepairIssueDTO>> getRepairIssueInfo(@PathVariable Long issueId) {
        RepairIssueDTO response = repairIssueService.getRepairIssueInfo(issueId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,"获取详细信息成功", response));
    }

    @Operation(summary = "通过用户ID查找某个用户上传的所有报修事件",description = "返回报修事件的详细信息")
    @GetMapping("/user/{userId}/issues")
    public ResponseEntity<ApiResponse<List<RepairIssueDTO>>> getResidentById(@PathVariable("userId") Long userId) {
        List<RepairIssue> repairIssues = repairIssueService.getRepairIssuesByUserId(userId);
        List<RepairIssueDTO> response = repairIssues.stream()
                .map(RepairIssueDTO::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "获取报修事件列表成功", response));
    }

    @Operation(summary = "上传报修事件",description = "这个接口无法使用swagger进行测试，需要使用Apifox，将body的" +
            "Content-Type设置为application/json，将image和video的Content-Type设置为multipart/form-data，才能测试成功")
    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Void>> uploadRepairIssue(
            @RequestPart(value="body") UploadRepairIssueRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "video", required = false) MultipartFile video) {
        repairIssueService.uploadRepairIssue(request, image, video);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "上传报修事件成功", null));
    }

}