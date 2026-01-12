package com.DOCKin.dto.Attendance;

import com.DOCKin.model.Attendance.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long id;
    private String userId;

    //출근,퇴근 시간
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    //근무시간
    private Duration totalWorkTime;

    //근로자 상태
    private String status;

    //출퇴근장소
    private String inLocation;
    private String OutLocation;

    public AttendanceDto(String inLocation) {
        this.inLocation = inLocation;
    }

    public static AttendanceDto fromEntity(Attendance saved) {
        return AttendanceDto.builder()
                .id(saved.getId())
                .userId(saved.getMember().getUserId())
                .clockInTime(saved.getClockInTime())
                .clockOutTime(saved.getClockOutTime())
                .status(saved.getStatus().name())
                .inLocation(saved.getInLocation())
                .OutLocation(saved.getOutLocation())
                .build();
    }
}
