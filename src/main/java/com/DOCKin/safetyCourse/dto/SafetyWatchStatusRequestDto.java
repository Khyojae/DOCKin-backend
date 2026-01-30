package com.DOCKin.safetyCourse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "근로자 교육 이수 상태 업데이트 요청")
public class SafetyWatchStatusRequestDto {

    @Schema(description = "교육 자료 id", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "교육 번호는 필수입니다.")
    private Integer courseId;

}