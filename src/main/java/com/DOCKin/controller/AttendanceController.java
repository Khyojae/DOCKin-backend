package com.DOCKin.controller;

import com.DOCKin.dto.AttendanceDto;
import com.DOCKin.dto.ClockInRequestDto;
import com.DOCKin.dto.ClockOutRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="근태관리",description = "근무자의 근태를 확인할 수 있음")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    //Clockin(출근 API)
    @Operation(summary="출근하기",description = "근무자가 어디서 출근했는지 찍힘")
    @PostMapping("/in")
    public ResponseEntity<AttendanceDto>ClockIn(@RequestBody ClockInRequestDto request){
        return ResponseEntity.ok(new AttendanceDto());
    }

    //Clockout(퇴근 API)
    @Operation(summary="퇴근하기",description = "근무자가 어디서 퇴근했는지 찍힘")
    @PostMapping("/out")
    public ResponseEntity<AttendanceDto>ClockOut(@RequestBody ClockOutRequestDto request){
        return ResponseEntity.ok(new AttendanceDto());
    }

    //개인 근태 기록 조회 API
    @Operation(summary="개인 근태 기록 조회",description ="근무자의 근태기록을 확인할 수 있음")
    @GetMapping
    public ResponseEntity<List<AttendanceDto>>GetMyAttendanceRecords(){
        return ResponseEntity.ok(null);
    }
}
