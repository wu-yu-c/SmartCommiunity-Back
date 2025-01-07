package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.dto.EventWithWorkerInfoDTO;
import com.example.SmartCommunity.model.EventEvaluation;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.EventEvaluationRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.EventEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventEvaluationImpl implements EventEvaluationService {
    @Autowired
    private EventEvaluationRepository eventEvaluationRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UserRepository userRepository;

    public EventEvaluation evaluateEvent(Long evaluatorId, Long staffId, String description, Integer score, String content) {
        // 验证 score 是否在 1 到 5 之间
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5.");
        }
        // 查找对应的 Evaluator 实体类
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        User evaluator = userRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 创建 EventEvaluation 实体类
        EventEvaluation eventEvaluation = new EventEvaluation();

        // 设置 Evaluator 实体（通过查找到的 Evaluator 对象）
        eventEvaluation.setStaff(staff);
        eventEvaluation.setUser(evaluator);
        eventEvaluation.setDescription(description); // 设置事件描述
        eventEvaluation.setScore(score); // 设置评分
        eventEvaluation.setContent(content); // 设置评价内容
        eventEvaluation.setCreatedTime(LocalDateTime.now()); // 设置评价时间为当前时间

        // 保存到数据库
        eventEvaluationRepository.save(eventEvaluation);
        //更新Staff表的平均分
        updateAverageRating(staffId);

        return eventEvaluation;
    }

    public void updateAverageRating(Long staffId) {
        // 计算新评分的平均值
        BigDecimal averageRating = eventEvaluationRepository.calculateAverageScoreByStaffId(staffId);

        // 更新 Staff 表
        Optional<Staff> optionalEvaluator = staffRepository.findById(staffId);
        if (optionalEvaluator.isPresent()) {
            Staff staff = optionalEvaluator.get();
            staff.setAverageRating(averageRating);
            staffRepository.save(staff);
        } else {
            throw new IllegalArgumentException("Evaluator not found for ID: " + staffId);
        }
    }

    public List<EventWithWorkerInfoDTO> getAllEvaluationsWithWorkerInfo() {
        try {
            return eventEvaluationRepository.findAllEventsWithWorkerInfo();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching event evaluations", e);
        }
    }

    public boolean deleteEvaluationByEventID(Long eventID) {
        // 使用 eventID 查找对应的评价
        Optional<EventEvaluation> evaluation = eventEvaluationRepository.findById(Math.toIntExact(eventID));

        // 如果评价存在，进行删除并返回 true；否则返回 false
        if (evaluation.isPresent()) {
            EventEvaluation eventEvaluation = evaluation.get();
            Long staffId = eventEvaluation.getStaff().getStaffID();
            eventEvaluationRepository.delete(evaluation.get());
            updateAverageRating(staffId);
            return true;
        }
        return false; // 如果未找到，返回 false
    }
}
