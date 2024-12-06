package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.model.Resident;
import com.example.SmartCommunity.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping
    public List<Resident> getAllResidents() {
        return residentService.findAll();
    }

    @GetMapping("/{id}")
    public Resident getResidentById(@PathVariable Integer id) {
        return residentService.findById(id);
    }

    @PostMapping
    public Resident createResident(@RequestBody Resident resident) {
        return residentService.save(resident);
    }

    @PutMapping("/{id}")
    public Resident updateResident(@PathVariable Long id, @RequestBody Resident resident) {
        resident.setId(id);
        return residentService.update(resident);
    }

    @DeleteMapping("/{id}")
    public void deleteResident(@PathVariable Integer id) {
        residentService.deleteById(id);
    }
}

// GET /api/residents：获取所有居民信息。
//
// GET /api/residents/{id}：根据 ID 获取指定居民信息。
//
// POST /api/residents：创建新的居民信息。
//
// PUT /api/residents/{id}：更新指定 ID 的居民信息。
//
// DELETE /api/residents/{id}：删除指定 ID 的居民信息。