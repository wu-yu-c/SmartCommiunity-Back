package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.model.RepairIssue;
import com.example.SmartCommunity.service.RepairIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-issues")
public class RepairIssueController {

    @Autowired
    private RepairIssueService repairIssueService;

    @GetMapping
    public List<RepairIssue> getAllRepairIssues() {
        return repairIssueService.findAll();
    }

    @GetMapping("/{id}")
    public RepairIssue getRepairIssueById(@PathVariable Integer id) {
        return repairIssueService.findById(id);
    }

    @PostMapping
    public RepairIssue createRepairIssue(@RequestBody RepairIssue repairIssue) {
        return repairIssueService.save(repairIssue);
    }

    @PutMapping("/{id}")
    public RepairIssue updateRepairIssue(@PathVariable Integer id, @RequestBody RepairIssue repairIssue) {
        repairIssue.setId(id);
        return repairIssueService.update(repairIssue);
    }

    @DeleteMapping("/{id}")
    public void deleteRepairIssue(@PathVariable Integer id) {
        repairIssueService.deleteById(id);
    }
}

//GET /api/repair-issues：获取所有维修问题。
//
//GET /api/repair-issues/{id}：根据 ID 获取指定维修问题。
//
//POST /api/repair-issues：创建新的维修问题。
//
//PUT /api/repair-issues/{id}：更新指定 ID 的维修问题。
//
//DELETE /api/repair-issues/{id}：删除指定 ID 的维修问题。