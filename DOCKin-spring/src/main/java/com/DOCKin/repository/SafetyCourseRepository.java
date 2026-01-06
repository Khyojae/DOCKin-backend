package com.DOCKin.repository;

import com.DOCKin.model.SafetyCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyCourseRepository extends JpaRepository<SafetyCourse, Integer> {
    List<SafetyCourse> findAllByCourseIdIn(List<Integer> courseIds);
}
