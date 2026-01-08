package com.DOCKin.controller;

import com.DOCKin.dto.AttendanceDto;
import com.DOCKin.dto.ClockInRequestDto;
import com.DOCKin.dto.ClockOutRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {
    //Clockin(출근 API)
    @PostMapping("/in")
    public ResponseEntity<AttendanceDto>ClockIn(@RequestBody ClockInRequestDto request){
        return ResponseEntity.ok(new AttendanceDto());
    }

    //Clockout(퇴근 API)
    @PostMapping("/out")
    public ResponseEntity<AttendanceDto>ClockOut(@RequestBody ClockOutRequestDto request){
        return ResponseEntity.ok(new AttendanceDto());
    }

    //개인 근태 기록 조회 API
    @GetMapping
    public ResponseEntity<List<AttendanceDto>>GetMyAttendanceRecords(){
        return ResponseEntity.ok(null);
    }
}
