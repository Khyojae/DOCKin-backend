package com.DOCKin.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "챗봇 dto")
public class ChatDomain {
    @Schema(description = "챗봇 request dto")
    public record Request(
            @Schema(description = "메시지 항목")
            List<Message> messages,

            @Schema(description = "언어")
            String lang,

            @Schema(description = "추적 ID")
            String traceId
    ){
        @Schema(description = "개별 메시지 상세")
        public record Message(
                @Schema(description = "역할")
                String role,

                @Schema(description = "메시지 내용")
                String content){}
    }

    @Schema(description = "챗봇 response dto")
    public record Response(
            @Schema(description = "추적 ID")
            String traceId,

            @Schema(description = "결과 데이터")
            Result result
    ){
        @Schema(description = "응답 결과 상세")
        public record Result(
                @Schema(description = "챗봇 답변 내용")
                String reply){}
    }
}
