package com.DOCKin.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "공통 에러 응답 dto")
public class ErrorResponseDto {
    @Schema(description = "HTTP 상태 코드", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
    private int status;

    @Schema(description = "에러 메시지", example = "잘못된 요청입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "에러 발생 시간", example = "2026-01-12T20:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime timestamp;
}
