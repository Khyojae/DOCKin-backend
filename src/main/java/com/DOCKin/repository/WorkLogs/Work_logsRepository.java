package com.DOCKin.repository.WorkLogs;

import com.DOCKin.model.Member.Member;
import com.DOCKin.model.WorkLogs.Work_logs;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Work_logsRepository extends JpaRepository<Work_logs, Long> {
    @Transactional
    Page<Work_logs> findByMemberIn(List<Member> members, Pageable pageable);
    Page<Work_logs> findAllByMemberUserId(String targetUserId, Pageable pageable);

    @Query("SELECT w FROM Work_logs w WHERE w.title LIKE %:keyword% OR w.logText LIKE %:keyword%")
    Page<Work_logs> searchWorkLogs(@Param("keyword") String keyword, Pageable pageable);
}
