package com.example.SmartCommunity.repository;

import com.example.SmartCommunity.dto.ReviewWithStaffInfoDTO;
import com.example.SmartCommunity.dto.ReviewDTO;
import com.example.SmartCommunity.model.StaffRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RatingRepository extends JpaRepository<StaffRating, Long> {
    @Query("SELECT AVG(e.score) FROM StaffRating e WHERE e.ratedStaff.staffId = :staffId")
    BigDecimal calculateAverageScoreByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT new com.example.SmartCommunity.dto.ReviewWithStaffInfoDTO(" +
            "e.id, e.score, e.content,e.ratingTime, " +
            "s.user.userName, s.user.avatar, s.department, s.post ) " +
            "FROM StaffRating e INNER JOIN e.ratedStaff s")
    List<ReviewWithStaffInfoDTO> findAllEventsWithWorkerInfo();

    @Query("SELECT new com.example.SmartCommunity.dto.ReviewDTO(" +
            "e.id, e.rater.id,  e.score, e.content, e.ratingTime) " +
            "FROM StaffRating e "+
            "WHERE e.ratedStaff.staffId = :staffId")
    List<ReviewDTO> findServicesByStaffId(@Param("staffId") Long staffId);
}
