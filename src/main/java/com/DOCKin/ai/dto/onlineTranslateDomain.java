package com.DOCKin.ai.dto;

public class onlineTranslateDomain {
    public record RtTranslateResponse(
            String traceId,
            TranslationResult result
    ){}

    public record TranslationResult(
            String recognizedText,
            String translatedText
    ){}
}
