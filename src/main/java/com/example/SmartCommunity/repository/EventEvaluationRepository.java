package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.dto.EventWithWorkerInfoDTO;
import com.example.SmartCommunity.dto.ServiceDTO;
import com.example.SmartCommunity.dto.StaffWithEvaluatedCount;
import com.example.SmartCommunity.model.EventEvaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import java.math.BigDecimal;

public interface EventEvaluationRepository extends JpaRepository<EventEvaluation, Integer> {
    @Query("SELECT AVG(e.Score) FROM EventEvaluation e WHERE e.staff.staffID = :staffID")
    BigDecimal calculateAverageScoreByStaffId(@Param("staffID") Long staffID);

    @Query("SELECT new com.example.SmartCommunity.dto.EventWithWorkerInfoDTO(" +
            "e.EventID, e.Description, e.Score, e.Content,e.CreatedTime, " +
            "COALESCE(s.name, ''), COALESCE(s.avatar, ''), COALESCE(s.position, ''), COALESCE(s.department, '') ) " +
            "FROM EventEvaluation e LEFT JOIN e.staff s")
    List<EventWithWorkerInfoDTO> findAllEventsWithWorkerInfo();

    @Query("SELECT new com.example.SmartCommunity.dto.ServiceDTO(" +
            "e.EventID, e.user.userID,  e.Description, e.Score, e.Content, e.CreatedTime) " +
            "FROM EventEvaluation e "+
            "WHERE e.staff.staffID = :staffID")
    List<ServiceDTO> findServicesByStaffId(@Param("staffID") Long staffID);


    @Query("SELECT new com.example.SmartCommunity.dto.StaffWithEvaluatedCount (" +
            "s.staffID, s.name, s.avatar, s.position, s.department, s.averageRating, COUNT(e.EventID)) " +
            "FROM Staff s " +
            "LEFT JOIN EventEvaluation e ON e.staff.staffID = s.staffID " +
            "GROUP BY s.staffID, s.name, s.avatar, s.position, s.department, s.averageRating")
    List<StaffWithEvaluatedCount> findStaffEvaluationCounts();
}
