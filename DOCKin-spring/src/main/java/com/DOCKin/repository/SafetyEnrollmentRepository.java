package com.DOCKin.repository;

import com.DOCKin.model.SafetyEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SafetyEnrollmentRepository extends JpaRepository<SafetyEnrollment, Integer> {
    Optional<SafetyEnrollment> findByUserIdAndCourseId(String userId, Integer courseId);

    @Query("SELECT se.courseId FROM SafetyEnrollment se WHERE se.userId = :userId AND se.isCompleted = FALSE")
    List<Integer> findUncompletedCourseIdsByUserId(@Param("userId") String userId);
}
