package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponseDTO;
import com.example.SmartCommunity.model.Repairissue;
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
import java.util.List;
import java.util.Map;

@Tag(name="报修接口")
@RestController
@RequestMapping("/api/repair-issues")
public class RepairIssueController {

    @Autowired
    private RepairIssueService repairIssueService;

    @GetMapping
    public List<Repairissue> getAllRepairIssues() {
        return repairIssueService.findAll();
    }

    // 不返回文件数据的接口（原有接口）
    @GetMapping("/{id}")
    public Repairissue getRepairIssueById(@PathVariable Integer id) {
        return repairIssueService.findById(id);
    }

    // 返回文件数据的接口（新接口）
    @GetMapping("/{id}/with-files")
    public ResponseEntity<RepairIssueResponseDTO> getRepairIssueWithFiles(@PathVariable Integer id) {
        try {
            RepairIssueResponseDTO response = repairIssueService.getRepairIssueWithFiles(id);
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Repairissue createRepairIssue(@RequestBody RepairIssueDTO repairIssue) {
        return repairIssueService.save(repairIssue);
    }

    @PostMapping(value = "/with-files", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Repairissue> createRepairIssueWithFiles(
            @RequestPart("repairIssue") RepairIssueDTO repairIssueDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "video", required = false) MultipartFile video) {
        try {
            Repairissue created = repairIssueService.createRepairIssueWithFiles(repairIssueDTO, image, video);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "获取所有保修地址")
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
    public Repairissue updateRepairIssue(@PathVariable Integer id, @RequestBody Repairissue repairIssue) {
        repairIssue.setId(id);
        return repairIssueService.update(repairIssue);
    }

    @DeleteMapping("/{id}")
    public void deleteRepairIssue(@PathVariable Integer id) {
        repairIssueService.deleteById(id);
    }
}