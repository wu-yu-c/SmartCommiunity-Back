package com.example.SmartCommunity.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SmartCommunity.dto.ReviewDTO;
import com.example.SmartCommunity.dto.StaffInfoDTO;
import com.example.SmartCommunity.dto.StaffWithReviewsDTO;
import com.example.SmartCommunity.model.Staff;
import com.example.SmartCommunity.model.StaffRating;
import com.example.SmartCommunity.repository.RatingRepository;
import com.example.SmartCommunity.repository.StaffRepository;
import com.example.SmartCommunity.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository, RatingRepository ratingRepository) {
        this.staffRepository = staffRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public StaffInfoDTO getStaffById(Long staffId) {
        Staff staff = staffRepository.findByStaffId(staffId).orElseThrow(()->new NoSuchElementException("要查询的职工不存在"));
        return new StaffInfoDTO(staff);
    }

    @Override
    public List<StaffInfoDTO> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        Long userId = StpUtil.getLoginIdAsLong();
        // 预先查询当前用户对所有职工的评分
        Map<Long, StaffRating> userRatings = ratingRepository
                .findByRater_Id(userId).stream()
                .collect(Collectors.toMap(
                        rating -> rating.getRatedStaff().getStaffId(),
                        rating -> rating
                ));

        // 组装结果
        return staffList.stream()
                .map(staff -> {
                    StaffInfoDTO dto = new StaffInfoDTO(staff);
                    // 查找当前用户对该职工的评分
                    StaffRating rating = userRatings.get(staff.getStaffId());
                    if (rating != null) {
                        dto.setUserRating(rating);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Staff> searchStaff(String name, String department) {
        // 如果人名和部门都为空，返回所有评价人员
        if (name == null && department == null) {
            return staffRepository.findAll();
        }

        return staffRepository.findByDepartmentContainingAndUser_UserNameContaining(department != null ? department : "",
                name != null ? name : "");
    }

    public StaffWithReviewsDTO getStaffWithServices() {
        Long userId = StpUtil.getLoginIdAsLong();
        Staff staff = staffRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("未找到该职工"));

        // 获取工作人员的评分信息
        List<ReviewDTO> reviews = ratingRepository.findServicesByStaffId(staff.getStaffId());

        return new StaffWithReviewsDTO(staff, reviews);
    }
}
