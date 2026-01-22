package com.DOCKin.repository.SafetyCourse;

import com.DOCKin.model.SafetyCourse.SafetyCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SafetyCourseRepository extends JpaRepository<SafetyCourse, Integer> {

    @Query("SELECT s FROM SafetyCourse s WHERE "
    +"LOWER(s.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
    "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SafetyCourse> searchByKeyword(String keyword, Pageable pageable);

    Page<SafetyCourse> findByCreatedBy(String createdBy, Pageable pageable);
}
