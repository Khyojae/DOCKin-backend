package com.DOCKin.DOCKin_spring.service;

import com.DOCKin.dto.Attendance.AttendanceDto;
import com.DOCKin.dto.Attendance.ClockInRequestDto;
import com.DOCKin.model.Attendance.AttendanceStatus;
import com.DOCKin.service.AttendanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AttendanceServiceTest {

  @Autowired
  private AttendanceService attendanceService;

  @Test
  @DisplayName("출근 예정 시간보다 늦게 오면 지각(LATE) 처리가 되어야 한다")
  void clockInLateTest() {
    String userId = "worker03";
    ClockInRequestDto requestDto = new ClockInRequestDto("제1조선소");


    AttendanceDto result = attendanceService.clockin(userId, requestDto);

    System.out.println("현재 상태: " + result.getStatus());


    assertEquals(AttendanceStatus.LATE, result.getStatus());
  }
}