package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.dto.EventWithWorkerInfoDTO;
import com.example.SmartCommunity.dto.StaffWithServicesDTO;
import com.example.SmartCommunity.model.EventEvaluation;
import com.example.SmartCommunity.service.EventEvaluationService;
import com.example.SmartCommunity.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Tag(name="事件评价接口")
@RestController
@RequestMapping("/api/EventEvaluation")
public class EventEvaluationController {

    @Autowired
    private EventEvaluationService eventEvaluationService;
    @Autowired
    private StaffService staffService;

    @Operation(summary = "用户上传对员工的评价", description = "用户上传对某次服务的评价，需要的参数为用户id，服务人员的id，" +
            "对本次服务内容的描述，对服务人员的评分以及评价的内容，上传成功后会同步更新该服务人员的平均分")
    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateEvent(
            @RequestParam Long evaluatorId,
            @RequestParam Long staffId,
            @RequestParam String description,
            @RequestParam Integer score,
            @RequestParam String content) {

        Map<String, Object> response = new HashMap<>();
        try {
            // 调用 service 层完成评价操作
            EventEvaluation eventEvaluation = eventEvaluationService.evaluateEvent(evaluatorId, staffId, description, score,
                    content);

            // 返回成功响应
            response.put("code", 200);
            response.put("staffID", staffId);
            response.put("description", description);
            response.put("score", score);
            response.put("content", content);
            response.put("message", "Evaluation submitted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 返回参数验证错误
            response.put("code", 400);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 返回异常信息
            response.put("code", 500);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "获取所有事件评价",description = "获取所有的事件评价，以及进行服务的工作人员基本信息")
    @GetMapping("/getAllEvaluation")
    public ResponseEntity<Map<String, Object>> getAllEvaluation() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取所有事件评价数据
            List<EventWithWorkerInfoDTO> evaluations = eventEvaluationService.getAllEvaluationsWithWorkerInfo();
            if (evaluations.isEmpty()) {
                // 事件评价为空
                response.put("message", "No event evaluations found.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            // 返回成功响应
            response.put("code",HttpStatus.OK.value());
            response.put("data", evaluations);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 捕获并处理异常
            response.put("status", "error");
            response.put("message", "An error occurred while fetching event evaluations.");
            // 记录异常日志
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "根据工作人员id获取其基本信息和所提供的所有服务")
    @GetMapping("/getEventEvaluationByStaffId/{staffId}")
    public ResponseEntity<Map<String, Object>> getEventEvaluationByStaffId(@PathVariable Long staffId){
        Map<String, Object> response = new HashMap<>();
        try {
            // 调用服务层获取数据
            StaffWithServicesDTO staffDetails = staffService.getStaffWithServices(staffId);

            // 返回成功响应
            response.put("code",HttpStatus.OK.value());
            response.put("data", staffDetails);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            // 处理工作人员ID不存在的情况
            response.put("status", "error");
            response.put("message", "Staff not found with ID: " + staffId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // 处理其他未预料到的异常
            response.put("status", "error");
            response.put("message", "An error occurred while fetching staff details.");
            // 记录异常日志
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary="根据EventID删除评价")
    @DeleteMapping("/deleteEvaluationByEventID/{EventID}")
    public ResponseEntity<Map<String, Object>> deleteEvaluationByEventID(@PathVariable Long EventID){
        Map<String, Object> response = new HashMap<>();
        try{
            boolean isDeleted = eventEvaluationService.deleteEvaluationByEventID(EventID);
            if (isDeleted) {
                // 删除成功
                response.put("code", HttpStatus.OK.value());
                response.put("message", "Event deleted successfully.");
                return ResponseEntity.ok(response);
            } else {
                // 如果未找到对应的 EventID
                response.put("code", HttpStatus.NOT_FOUND.value());
                response.put("message", "Event evaluation not found for EventID: " + EventID);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        }catch(Exception e){
            response.put("status", "error");
            response.put("message", "An error occurred while fetching event evaluations.");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // 根据评价ID删除评价
    // 根据用户ID查看某用户进行的所有评价
    // 根据工作人员ID查看该工作人员收到的所有评价
}
