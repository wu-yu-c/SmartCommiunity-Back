package com.example.SmartCommunity.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SmartCommunity.dto.ReviewRequest;
import com.example.SmartCommunity.dto.ReviewWithStaffInfoDTO;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.StaffRating;
import com.example.SmartCommunity.model.User;
import com.example.SmartCommunity.repository.RatingRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.repository.UserRepository;
import com.example.SmartCommunity.service.RatingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final StaffRepository staffRepository;

    private final UserRepository userRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, StaffRepository staffRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    public void uploadReview(ReviewRequest request) {
        StaffRating rating = new StaffRating();
        BeanUtils.copyProperties(request, rating);

        Staff staff = staffRepository.findByStaffId(request.getStaffId())
                .orElseThrow(() -> new NoSuchElementException("未找到该职工"));
        rating.setRatedStaff(staff);

        User user = userRepository.findUserById(StpUtil.getLoginIdAsLong());
        rating.setRater(user);

        if (Objects.equals(staff.getId(), user.getId()))
            throw new SecurityException("不可以对自己进行评分");

        if (ratingRepository.existsByRatedStaffAndRater(staff, user))
            throw new SecurityException("不可以对同一职工重复评分");

        ratingRepository.save(rating);
        updateAverageRating(request.getStaffId());
    }

    public void updateAverageRating(Long staffId) {
        // 计算新评分的平均值
        BigDecimal averageRating = ratingRepository.calculateAverageScoreByStaffId(staffId);
        Staff staff = staffRepository.findStaffByStaffId(staffId);
        staff.setAverageRating(averageRating);
        staffRepository.save(staff);
    }

    public List<ReviewWithStaffInfoDTO> getAllEvaluationsWithWorkerInfo() {
        return ratingRepository.findAllEventsWithWorkerInfo();
    }

    public void deleteRatingById(Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        StaffRating rating = ratingRepository.findById(id).orElseThrow(() -> new NoSuchElementException("未找到该评价"));
        if(!rating.getRater().getId().equals(userId))
            throw new SecurityException("您无权删除他人的评价");
        ratingRepository.delete(rating);
        updateAverageRating(rating.getRatedStaff().getStaffId());
    }
}
