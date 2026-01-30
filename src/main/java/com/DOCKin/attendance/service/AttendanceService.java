package com.DOCKin.attendance.service;

import com.DOCKin.attendance.dto.AttendanceDto;
import com.DOCKin.attendance.dto.ClockInRequestDto;
import com.DOCKin.attendance.dto.ClockOutRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.attendance.model.Attendance;
import com.DOCKin.attendance.model.AttendanceStatus;
import com.DOCKin.member.model.Member;
import com.DOCKin.attendance.repository.AttendanceRepository;
import com.DOCKin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.DOCKin.attendance.dto.AttendanceDto.fromEntity;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;


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
            throw new IllegalArgumentException("이미 오늘 출근 처리가 완료되었습니다.");
        }

        // 출근 상태 수정
        AttendanceStatus status = AttendanceStatus.NORMAL;
        if(now.toLocalTime().isAfter(java.time.LocalTime.of(9,0))){
            status=AttendanceStatus.LATE;
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
        return fromEntity(saved);
    }

    //퇴근로직
    @Transactional
    public AttendanceDto clockout(String userId, ClockOutRequestDto dto){
        //멤버 존재 확인
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        //가장 마지막의 출근 기록을 가져옴
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByMemberAndWorkDate(member,today)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(attendance.getClockOutTime()!=null){
            throw new IllegalArgumentException("출근 기록이 존재하지 않습니다.");
        }

        //시간 갱신
        LocalDateTime now = LocalDateTime.now();
        attendance.setClockOutTime(LocalDateTime.now());
        attendance.setOutLocation(dto.getOutLocation());

        java.time.Duration duration = java.time.Duration.between(attendance.getClockInTime(),now);
        long h = duration.toHours();
        long m = duration.toMinutesPart();
        long s = duration.toSecondsPart();

        String timeString = String.format("%02d:%02d:%02d", h, m, s);
        attendance.setTotalWorkTime(timeString);

        return fromEntity(attendance);
    }

    //개인 출퇴 기록 조회
    @Transactional(readOnly = true)
    public List<AttendanceDto> getMyAttendanceRecords(String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<Attendance> records = attendanceRepository.findByMemberOrderByWorkDateDesc(member);

        return records.stream()
                .map(AttendanceDto::fromEntity)
                .collect(Collectors.toList());
    }
}
