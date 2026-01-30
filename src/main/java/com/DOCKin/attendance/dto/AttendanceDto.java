package com.DOCKin.attendance.dto;

import com.DOCKin.attendance.model.Attendance;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description="출퇴 조회용 dto")
public class AttendanceDto {

    @Schema(description = "그냥 조회용",requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "사원번호", example = "1001",requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    //출근,퇴근 시간
    @Schema(description = "출근한 시간", example = "2026-01-01 09:00:00",requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clockInTime;

    @Schema(description = "퇴근한 시간", example = "2026-01-01 09:00:00",requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime clockOutTime;

    //근무시간
    @Schema(description = "총 근무 시간 (HH:mm:ss 형식)", example = "08:30:00",requiredMode = Schema.RequiredMode.REQUIRED)
    private String totalWorkTime;

    //근로자 상태
    @Schema(description = "근태 상태 (NORMAL, LATE, EARLY_LEAVE 등)", example = "NORMAL",requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    //출퇴근장소
    @Schema(description = "출근 장소", example = "제1조선소",requiredMode = Schema.RequiredMode.REQUIRED)
    private String inLocation;

    @Schema(description = "퇴근 장소", example = "제1조선소",requiredMode = Schema.RequiredMode.REQUIRED)
    private String outLocation;

    public static AttendanceDto fromEntity(Attendance saved) {
        return AttendanceDto.builder()
                .id(saved.getId())
                .userId(saved.getMember().getUserId())
                .clockInTime(saved.getClockInTime())
                .clockOutTime(saved.getClockOutTime())
                .totalWorkTime(saved.getTotalWorkTime())
                .status(saved.getStatus().name())
                .inLocation(saved.getInLocation())
                .outLocation(saved.getOutLocation())
                .build();
    }
}
