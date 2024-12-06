package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.EvaluatorService;
import com.example.SmartCommunity.dto.EvaluatorResponseDTO;
import com.example.SmartCommunity.model.Evaluator;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/Evaluators")
public class EvaluatorController {

    private final EvaluatorService evaluatorService;

    public EvaluatorController(EvaluatorService evaluatorService) {
        this.evaluatorService = evaluatorService;
    }

    @Operation(summary = "获取评价人员列表")
    @GetMapping("/all")
    public ResponseEntity<List<EvaluatorResponseDTO>> getEvaluators() {
        try {
            List<Evaluator> evaluators = evaluatorService.getAllEvaluators();
            List<EvaluatorResponseDTO> responseDTOs = evaluators.stream()
                    .map(evaluator -> new EvaluatorResponseDTO(
                            evaluator.getEvaluatorID(),
                            evaluator.getName(),
                            evaluator.getPosition(),
                            evaluator.getAvatar()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "根据人名和部门筛选评价人员")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchEvaluators(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {

        try {
            List<Evaluator> evaluators = evaluatorService.searchEvaluators(name, department);
            List<EvaluatorResponseDTO> responseDTOs = evaluators.stream()
                    .map(evaluator -> new EvaluatorResponseDTO(
                            evaluator.getEvaluatorID(),
                            evaluator.getName(),
                            evaluator.getPosition(),
                            evaluator.getAvatar()))
                    .collect(Collectors.toList());

            // 返回的响应包含查询结果数量和结果列表
            return ResponseEntity.ok(Map.of(
                    "count", evaluators.size(),
                    "evaluators", responseDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询出错，请稍后再试"));
        }
    }

    // 异常处理方法
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "内部服务器错误", "message", ex.getMessage()));
    }
}
