package com.DOCKin.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stt dto")
public class SttDomain {
    @Schema(description = "stt request")
    public record Request(

            @Schema(description = "바꿀 언어")
            String lang,

            @Schema(description = "추적ID")
            String traceId
    ){}

    @Schema(description = "stt response")
    public record Response(

            @Schema(description = "추적ID")
            String traceId,

            @Schema(description = "변환된 text")
            String text
    ){}
}
