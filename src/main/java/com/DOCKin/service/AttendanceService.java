package com.DOCKin.service;

import com.DOCKin.dto.Attendance.AttendanceDto;
import com.DOCKin.dto.Attendance.ClockInRequestDto;
import com.DOCKin.dto.Attendance.ClockOutRequestDto;
import com.DOCKin.global.enums.Status;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Attendance.Attendance;
import com.DOCKin.model.Attendance.AttendanceStatus;
import com.DOCKin.model.Member.Member;
import com.DOCKin.repository.AttendanceRepository;
import com.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    //1일 1출근 가정

    //출근로직
    @Transactional
    public AttendanceDto clockin(String userId, ClockInRequestDto dto) {
        // 멤버 존재 확인
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 날짜 갱신
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        //오류
        if(attendanceRepository.findByMemberAndWorkDate(member,today).isPresent()){
            throw new IllegalArgumentException("이미 오늘 출근 처리가 완료되었습니다");
        }

        // 출근 상태 수정
        AttendanceStatus status = AttendanceStatus.NORMAL;
        if (now.toLocalTime().isAfter(java.time.LocalTime.of(9, 0))) {
            status = AttendanceStatus.LATE;
        }

        // 객체 생성
        Attendance attendance = Attendance.builder()
                .member(member)
                .clockInTime(now)
                .workDate(today)
                .status(status)
                .inLocation(dto.getInLocation())
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceDto.fromEntity(saved);
    }

    //퇴근로직
    @Transactional
    public AttendanceDto clockout(String userId, ClockOutRequestDto dto){
        //멤버 존재 확인
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //시간 갱신
        LocalDateTime now =LocalDateTime.now();
        LocalDate todayDate = now.toLocalDate();

        //오류
        Attendance attendance = attendanceRepository.findByMemberAndWorkDate(member,todayDate)
                .orElseThrow(()->new IllegalArgumentException("출근처리를 먼저하세요."));

        if(attendance.getClockOutTime()!=null){
            throw new IllegalArgumentException("이미 오늘 퇴근 처리가 완료되었습니다.");
        }
        attendance.recordClockOut(now,dto.getOutLocation(),AttendanceStatus.NORMAL);
        return AttendanceDto.fromEntity(attendance);
    }
}
