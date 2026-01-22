package com.DOCKin.dto.WorkLogs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작업 일지 수정 req dto")
public class WorkLogsUpdateRequestDto {
    @Schema(description = "일지 제목",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String title;

    @Schema(description = "일지 상세 내용", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    private String logText;

    @Schema(description = "첨부 이미지 URL (변경 시에만 입력)")
    private String imageUrl;

    @Schema(description = "관련 장비 고유 ID", example = "50")
    private Long equipmentId;

    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
}
