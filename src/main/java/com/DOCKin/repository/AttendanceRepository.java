package com.DOCKin.repository;

import com.DOCKin.model.Attendance.Attendance;
import com.DOCKin.model.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByMemberOrderByWorkDateDesc(Member member);
    Optional<Attendance> findByMemberAndWorkDate(Member member, LocalDate workDate);
    Optional<Attendance> findFirstByMemberOrderByClockInTimeDesc(Member member);
    List<Attendance> findByMemberAndWorkDateBetween(Member member, LocalDate startDate, LocalDate endDate);
}
