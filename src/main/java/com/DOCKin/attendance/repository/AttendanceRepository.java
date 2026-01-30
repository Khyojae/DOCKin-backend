package com.DOCKin.attendance.repository;

import com.DOCKin.attendance.model.Attendance;
import com.DOCKin.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByMemberOrderByWorkDateDesc(Member member);
    Optional<Attendance> findByMemberAndWorkDate(Member member, LocalDate workDate);
}
