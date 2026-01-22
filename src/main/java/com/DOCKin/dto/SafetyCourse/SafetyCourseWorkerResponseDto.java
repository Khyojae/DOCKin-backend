package com.DOCKin.dto.SafetyCourse;

import com.DOCKin.model.SafetyCourse.SafetyCourse;
import com.DOCKin.model.SafetyCourse.SafetyEnrollment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "근로자용 안전 교육 목록 응답 DTO")
public class SafetyCourseWorkerResponseDto {
    @Schema(description = "교육 자료 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer courseId;

    @Schema(description = "교육 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "교육 영상 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String videoUrl;

    @Schema(description = "교육 소요 시간 (분)", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer durationMinutes;

    @Schema(description = "시청 상태", requiredMode = Schema.RequiredMode.REQUIRED)
    private CompletedLabel status;

    @Schema(description = "이수 날짜", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime completionDate;

    public static SafetyCourseWorkerResponseDto from(SafetyCourse safetyCourse,SafetyEnrollment enrollment) {
        return SafetyCourseWorkerResponseDto.builder()
                .courseId(safetyCourse.getCourseId()).
                title(safetyCourse.getTitle())
                .videoUrl(safetyCourse.getVideoUrl())
                .durationMinutes(safetyCourse.getDurationMinutes())
                .status(enrollment != null ? enrollment.getStatus() : CompletedLabel.UNWATCHED)
                .completionDate(enrollment != null ? enrollment.getCompletion_date() : null)
                .build();
    }
}