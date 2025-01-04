package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.model.EventEvaluation;
import com.example.SmartCommunity.service.EventEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name="事件评价接口")
@RestController
@RequestMapping("/api/EventEvaluation")
public class EventEvaluationController {

    @Autowired
    private EventEvaluationService eventEvaluationService;

    @Operation(summary = "用户上传对事件的评价", description = "用户上传对某次服务的评价，需要的参数为用户id，服务人员的id，" +
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

    // 根据评价ID删除评价
    // 根据用户ID查看某用户进行的所有评价
    // 根据工作人员ID查看该工作人员收到的所有评价
}
