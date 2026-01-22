package com.DOCKin.dto.WorkLogs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작업 일지 생성 req dto")
public class WorkLogsCreateRequestDto {
    @Schema(description = "일지 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @Schema(description = "일지 상세 내용", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String logText;

    @Schema(description = "첨부 이미지 URL")
    private String imageUrl;

    @Schema(description = "관련 장비 고유 ID", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "관련 장비 ID는 필수입니다.")
    private Long equipmentId;

    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
}
