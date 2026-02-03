package com.DOCKin.ai.dto;

public class TranslateDomain{
    //작업일지 번역
    public record Request(
                             String source,
                             String target,
                             String traceId){}

    //실시간 번역 (원문이 있을때 사용)
    public record ApiRequest(
            String text,
            String source,
            String target,
            String traceId
    ){}


    //작업일지 응답 
    public record Response(
            String title,
            String translated,
            String model,
            String traceId){}
}