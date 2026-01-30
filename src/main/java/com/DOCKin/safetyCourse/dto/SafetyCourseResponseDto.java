package com.DOCKin.safetyCourse.dto;

import com.DOCKin.safetyCourse.model.SafetyCourse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "안전 교육 상세 정보 res dto")
public class SafetyCourseResponseDto {
    @Schema(description = "교육 자료 id", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer courseId;

    @Schema(description = "작성자 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "교육 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "교육 상세 설명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "교육 영상 주소", requiredMode = Schema.RequiredMode.REQUIRED)
    private String videoUrl;

    @Schema(description = "교육 참고 자료 URL (PDF 등)")
    private String materialUrl;

    @Schema(description = "강의 소요 시간 (분)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer durationMinutes;


    public static SafetyCourseResponseDto fromEntity(SafetyCourse saved){
        return SafetyCourseResponseDto.builder()
                .courseId(saved.getCourseId())
                .userId(saved.getCreatedBy())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .videoUrl(saved.getVideoUrl())
                .durationMinutes(saved.getDurationMinutes())
                .build();
    }
}
