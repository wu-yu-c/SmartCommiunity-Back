package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.Evaluator;
import com.example.SmartCommunity.model.EventEvaluation;
import com.example.SmartCommunity.repository.EvaluatorRepository;
import com.example.SmartCommunity.repository.EventEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class EventEvaluationService {

    @Autowired
    private EventEvaluationRepository eventEvaluationRepository;
    @Autowired
    private EvaluatorRepository evaluatorRepository;
    public EventEvaluation evaluateEvent(Long evaluatorId, String description, Integer score, String content) {
        // 验证 score 是否在 1 到 5 之间
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5.");
        }
        // 查找对应的 Evaluator 实体类
        Evaluator evaluator = evaluatorRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("Evaluator not found"));

        // 创建 EventEvaluation 实体类
        EventEvaluation eventEvaluation = new EventEvaluation();

        // 设置 Evaluator 实体（通过查找到的 Evaluator 对象）
        eventEvaluation.setEvaluator(evaluator);
        eventEvaluation.setDescription(description); // 设置事件描述
        eventEvaluation.setScore(score); // 设置评分
        eventEvaluation.setContent(content); // 设置评价内容
        eventEvaluation.setCreatedTime(LocalDateTime.now()); // 设置评价时间为当前时间

        // 保存到数据库
        return eventEvaluationRepository.save(eventEvaluation);
    }
}
