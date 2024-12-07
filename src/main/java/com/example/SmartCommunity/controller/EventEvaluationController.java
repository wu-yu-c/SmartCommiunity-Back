package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.model.EventEvaluation;
import com.example.SmartCommunity.service.EventEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventEvaluationController {

    @Autowired
    private EventEvaluationService eventEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<Map<String, Object>> evaluateEvent(
            @RequestParam Long evaluatorId,
            @RequestParam String description,
            @RequestParam Integer score,
            @RequestParam String content) {

        Map<String, Object> response = new HashMap<>();
        try {
            // 调用 service 层完成评价操作
            EventEvaluation eventEvaluation = eventEvaluationService.evaluateEvent(evaluatorId, description, score,
                    content);

            // 返回成功响应
            response.put("code", 200);
            response.put("evaluatorID", evaluatorId);
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
}
