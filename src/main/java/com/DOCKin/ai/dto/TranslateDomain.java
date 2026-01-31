package com.DOCKin.ai.dto;

public class TranslateDomain{
    public record Request(   String text,
                             String source,
                             String target,
                             String traceId){}

    public record Response(
            String traceId,
            Result result
    ){
        public record Result(String translated){}
    }
}
