package com.DOCKin.ai.dto;

public class SttDomain {
    public record Request(
            String lang,
            String traceId
    ){}

    public record Response(
            String traceId,
            String text
    ){}
}
