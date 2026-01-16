package com.DOCKin.repository.SafetyCourse;

import com.DOCKin.model.SafetyCourse.SafetyCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyCourseRepository extends JpaRepository<SafetyCourse, Integer> {
    List<SafetyCourse> findAllByCourseIdIn(List<Integer> courseIds);
}
