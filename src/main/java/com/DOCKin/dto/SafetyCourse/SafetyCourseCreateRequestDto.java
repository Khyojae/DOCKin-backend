package com.DOCKin.dto.SafetyCourse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "안전 교육 생성 req dto")
public class SafetyCourseCreateRequestDto {

    @Schema(description = "교육 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "교육 제목은 필수입니다.")
    private String title;

    @Schema(description = "교육 상세 설명",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "교육 설명은 필수입니다.")
    private String description;

    @Schema(description = "교육 영상 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "영상 주소는 필수입니다.")
    private String videoUrl;

    @Schema(description = "교육 참고 자료 URL (PDF 등)")
    private String materialUrl;

    @Schema(description = "교육 소요 시간 (분)", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive(message = "교육 시간은 0보다 커야 합니다.")
    private Integer durationMinutes;

}
