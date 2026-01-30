package com.DOCKin.safetyCourse.repository;

import com.DOCKin.safetyCourse.model.SafetyEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface SafetyEnrollmentRepository extends JpaRepository<SafetyEnrollment, Integer> {

    List<SafetyEnrollment> findAllByUserIdUserId(String userId);

    Optional<SafetyEnrollment> findByUserIdUserIdAndCourseIdCourseId(String userId, Integer courseId);
}
