package com.DOCKin.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "번역 dto")
public class TranslateDomain{

    @Schema(description = "작업일지 번역 request")
    public record Request(
            @Schema(description = "원래 언어")
                             String source,

                             @Schema(description = "바꿀 언어")
                             String target,

                             @Schema(description = "추적ID")
                             String traceId){}

    @Schema(description = "실시간 번역 (원문이 있을때 사용) request")
    public record ApiRequest(

            @Schema(description = "원래 문장")
            String text,

            @Schema(description = "원래 언어")
            String source,

            @Schema(description = "바꿀 언어")
            String target,

            @Schema(description = "추적ID")
            String traceId
    ){}

    @Schema(description = "작업일지 response")
    public record Response(

            @Schema(description = "작업일지 제목")
            String title,

            @Schema(description = "번역된 문장")
            String translated,

            @Schema(description = "모델명")
            String model,

            @Schema(description = "추적ID")
            String traceId){}
}