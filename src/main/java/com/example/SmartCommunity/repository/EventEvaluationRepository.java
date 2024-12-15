package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.model.EventEvaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface EventEvaluationRepository extends JpaRepository<EventEvaluation, Integer> {
    @Query("SELECT AVG(e.Score) FROM EventEvaluation e WHERE e.staff.staffID = :staffID")
    BigDecimal calculateAverageScoreByStaffId(@Param("staffID") Long staffID);
}
