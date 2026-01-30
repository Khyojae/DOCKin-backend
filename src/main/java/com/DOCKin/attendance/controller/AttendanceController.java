package com.DOCKin.attendance.controller;

import com.DOCKin.attendance.dto.AttendanceDto;
import com.DOCKin.attendance.dto.ClockInRequestDto;
import com.DOCKin.attendance.dto.ClockOutRequestDto;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.attendance.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="근태관리",description = "근무자의 근태를 확인할 수 있음")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @Operation(summary="출근하기",description = "근무자가 어디서 출근했는지 찍힘")
    @PostMapping("/in")
    public ResponseEntity<AttendanceDto>ClockIn(@AuthenticationPrincipal CustomUserDetails userDetails,
           @Valid @RequestBody ClockInRequestDto request){
        String userId = userDetails.getMember().getUserId();
        AttendanceDto response =attendanceService.clockin(userId,request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="퇴근하기",description = "근무자가 어디서 퇴근했는지 찍힘")
    @PostMapping("/out")
    public ResponseEntity<AttendanceDto>ClockOut(@AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ClockOutRequestDto request){
      String userId = userDetails.getMember().getUserId();
        AttendanceDto response = attendanceService.clockout(userId,request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary="개인 근태 기록 조회",description ="근무자의 근태기록을 확인할 수 있음")
    @GetMapping
    public ResponseEntity<List<AttendanceDto>>GetMyAttendanceRecords(@AuthenticationPrincipal CustomUserDetails userDetails){
        String userId = userDetails.getMember().getUserId();
        List<AttendanceDto> responses = attendanceService.getMyAttendanceRecords(userId);
        return ResponseEntity.ok(responses);
    }
}
