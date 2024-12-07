package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.EventEvaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface EventEvaluationRepository extends JpaRepository<EventEvaluation, Integer> {
    // 你可以在这里添加一些查询方法（如果需要的话）
    @Query("SELECT AVG(e.Score) FROM EventEvaluation e WHERE e.evaluator.evaluatorID = :evaluatorId")
    BigDecimal calculateAverageScoreByEvaluatorId(@Param("evaluatorId") Long evaluatorId);

}