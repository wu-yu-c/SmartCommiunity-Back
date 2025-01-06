package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.EventWithWorkerInfoDTO;
import com.example.SmartCommunity.model.EventEvaluation;

import java.util.List;

public interface EventEvaluationService {
    EventEvaluation evaluateEvent(Long evaluatorId, Long staffId, String description, Integer score, String content);

    void updateAverageRating(Long staffId);

    List<EventWithWorkerInfoDTO> getAllEvaluationsWithWorkerInfo();

    boolean deleteEvaluationByEventID(Long eventID);
}
