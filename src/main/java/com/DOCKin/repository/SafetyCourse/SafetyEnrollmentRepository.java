package com.DOCKin.repository.SafetyCourse;

import com.DOCKin.model.SafetyCourse.SafetyEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface SafetyEnrollmentRepository extends JpaRepository<SafetyEnrollment, Integer> {

    List<SafetyEnrollment> findAllByUserId_UserId(String userId);

    Optional<SafetyEnrollment> findByUserId_UserIdAndCourseId_CourseId(String userId, Integer courseId);
}
