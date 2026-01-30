package com.DOCKin.ai.dto;

import java.util.List;

public class ChatDomain {
    public record Request(
            List<Message> messages,
            String lang,
            String traceId
    ){
        public record Message(String role, String content){}
    }

    public record Response(
            String traceId,
            Result result
    ){
        public record Result(String reply){}
    }
}
