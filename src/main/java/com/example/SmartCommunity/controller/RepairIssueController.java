package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.RepairIssueDTO;
import com.example.SmartCommunity.dto.RepairIssueResponse;
import com.example.SmartCommunity.model.Repairissue;
import com.example.SmartCommunity.service.RepairIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<RepairIssueResponse> getRepairIssueWithFiles(@PathVariable Integer id) {
        try {
            RepairIssueResponse response = repairIssueService.getRepairIssueWithFiles(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Repairissue createRepairIssue(@RequestBody Repairissue repairIssue) {
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