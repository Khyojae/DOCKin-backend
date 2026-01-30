package com.DOCKin.attendance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "출근지역 표시")
public class ClockInRequestDto {
    @Schema(description = "출근지역 표시",example = "제1조선소", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "출근 장소는 필수 입력 사항입니다.")
    private String inLocation;
}
