package com.DOCKin.worklog.dto;

import com.DOCKin.worklog.model.Work_logs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "작업 일지 조회 res dto")
public class Work_logsDto {
    @Schema(description = "작업 일지 고유 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long logId;

    @Schema(description = "작성자 사원번호",requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "관련 장비 ID", example = "50")
    private Long equipmentId;

    @Schema(description = "일지 제목",requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "일지 상세 내용")
    private String logText;

    @Schema(description = "첨부 이미지 URL")
    private String imageUrl;

    @Schema(description = "작성 일시", example = "2026-01-12T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2026-01-12T15:30:00")
    private LocalDateTime updatedAt;

    private String audioFileUrl;

    public static Work_logsDto from(Work_logs entity) {
        return Work_logsDto.builder()
                .logId(entity.getLogId())
                .userId(entity.getMember().getUserId())
                .equipmentId(entity.getEquipment().getEquipmentId())
                .title(entity.getTitle())
                .logText(entity.getLogText())
                .imageUrl(entity.getImageUrl())
                .audioFileUrl(entity.getAudioFileUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
