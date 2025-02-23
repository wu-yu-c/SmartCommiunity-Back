package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponseDTO;
import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.service.RepairIssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="报修接口")
@RestController
@RequestMapping("/api/repair-issues")
public class RepairIssueController {

    @Autowired
    private RepairIssueService repairIssueService;

    @GetMapping
    public List<RepairIssue> getAllRepairIssues() {
        return repairIssueService.findAll();
    }

    @Operation(summary = "通过事件ID获取报修事件的详情",description = "传入事件ID，返回该事件的所有信息")
    @GetMapping("/{id}/with-files")
    public ResponseEntity<RepairIssueResponseDTO> getRepairIssueInfo(@PathVariable Integer id) {
        try {
            RepairIssueResponseDTO response = repairIssueService.getRepairIssueInfo(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "通过居民ID查找报修事件",description = "参数为residentId，实际上是user表中的userId，返回报修事件中的所有字段")
    @GetMapping("/getRepairIssue/{residentId}")
    public ResponseEntity<?> getResidentById(@PathVariable("residentId") Long residentId) {
        try{
            List<RepairIssueResponseDTO> response = repairIssueService.getRepairIssueByResidentId(residentId);
            if (response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No data found for residentId: " + residentId));
            }
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "上传报修事件")
    @PostMapping(value = "/uploadRepairIssue")
    public ResponseEntity<?> createRepairIssueWithFiles(
            @RequestParam("residentID") Long residentID,
            @RequestParam("repairIssueDetails") String repairIssueDetails,
            @RequestParam("repairAddress") String repairAddress,
            @RequestParam("repairIssueTitle") String repairIssueTitle,
            @RequestParam("repairIssueCategory") String repairIssueCategory,
            @RequestParam("repairIssueStatus") String repairIssueStatus,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "video", required = false) MultipartFile video) {
        Map<String, Object> response = new HashMap<>();
        try {
            RepairIssueDTO repairIssueDTO = new RepairIssueDTO(residentID, repairIssueDetails, repairAddress,
                    repairIssueTitle, repairIssueCategory, repairIssueStatus);
            RepairIssue created = repairIssueService.createRepairIssue(repairIssueDTO, image, video);
            response.put("code", HttpStatus.CREATED.value());
            response.put("data", created);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 设置失败响应内容
            response.put("code", 400);
            response.put("message", "Upload failed: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "获取所有报修地址")
    @GetMapping("/getAllRepairAddresses")
    public ResponseEntity<List<String>> getAllRepairAddresses() {
        try {
            List<String> addresses = repairIssueService.getAllRepairAddresses();
            if (addresses.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            // 异常处理，记录日志等
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PutMapping("/{id}")
    public RepairIssue updateRepairIssue(@PathVariable Long id, @RequestBody RepairIssue repairIssue) {
        repairIssue.setId(id);
        return repairIssueService.update(repairIssue);
    }

    @DeleteMapping("/{id}")
    public void deleteRepairIssue(@PathVariable Integer id) {
        repairIssueService.deleteById(id);
    }
}