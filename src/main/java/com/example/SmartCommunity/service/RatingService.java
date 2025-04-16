package com.example.SmartCommunity.service;

import com.example.SmartCommunity.dto.ReviewRequest;
import com.example.SmartCommunity.dto.ReviewWithStaffInfoDTO;

import java.util.List;

public interface RatingService {
    void uploadReview(ReviewRequest request);

    List<ReviewWithStaffInfoDTO> getAllEvaluationsWithWorkerInfo();

    void deleteRatingById(Long id);
}
