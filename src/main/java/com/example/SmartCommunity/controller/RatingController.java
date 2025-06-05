package com.example.SmartCommunity.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.dto.ReviewWithStaffInfoDTO;
import com.example.SmartCommunity.dto.ReviewRequest;
import com.example.SmartCommunity.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="评分接口")
@RestController
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "用户上传对员工的评价", description = "用户上传对某次服务的评价，需要的参数为职工的工号，" +
            "对本次服务内容的描述，对服务人员的评分以及评价的内容，上传成功后会同步更新该服务人员的平均分")
    @PostMapping()
    @SaCheckLogin
    public ResponseEntity<ApiResponse<Void>> uploadReview(@RequestBody ReviewRequest request) {
        ratingService.uploadReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "评分上传成功", null));
    }

    @Operation(summary = "获取所有评分",description = "获取所有的评分记录，以及被评分的的工作人员基本信息")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ReviewWithStaffInfoDTO>>> getAllEvaluation() {
        // 获取所有事件评价数据
        List<ReviewWithStaffInfoDTO> response = ratingService.getAllEvaluationsWithWorkerInfo();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,"评分记录获取成功",response));
    }

    @Operation(summary="根据评分的编号(主码)删除评价")
    @DeleteMapping("/{id}")
    @SaCheckLogin
    public ResponseEntity<ApiResponse<Void>> deleteEvaluationByEventID(@PathVariable Long id) {
        ratingService.deleteRatingById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "删除成功", null));
    }
}
