package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.EventEvaluation;

public interface EventEvaluationService {
    EventEvaluation evaluateEvent(Long evaluatorId, Long staffId, String description, Integer score, String content);

    void updateAverageRating(Long staffId);
}
