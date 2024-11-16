package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.EventEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventEvaluationRepository extends JpaRepository<EventEvaluation, Integer> {
    // 你可以在这里添加一些查询方法（如果需要的话）
}