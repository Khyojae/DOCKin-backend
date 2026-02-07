package com.DOCKin.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "실시간 stt dto")
public class onlineTranslateDomain {
    @Schema(description = "실시간 stt 번역 response")
    public record RtTranslateResponse(
            @Schema(description = "추적ID")
            String traceId,

            @Schema(description = "응답 항목 데이터")
            TranslationResult result
    ){}

    @Schema(description = "응답 항목 상세")
    public record TranslationResult(
            @Schema(description = "원문")
            String recognizedText,

            @Schema(description = "번역된 문장")
            String translatedText
    ){}
}
