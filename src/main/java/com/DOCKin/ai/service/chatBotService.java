package com.DOCKin.ai.service;

import com.DOCKin.ai.dto.ChatDomain;
import com.DOCKin.ai.model.ChatLog;
import com.DOCKin.ai.repository.ChatLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class chatBotService {
    private final ChatLogRepository chatLogRepository;
    private final WebClient fastApiWebClient;

@Transactional
    public ChatDomain.Response chatBotFromSpringToFastApi(ChatDomain.Request request,String userId){

       ChatDomain.Response.Result apiResult =fastApiWebClient.post()
               .uri("/api/chatbot")
               .bodyValue(request)
               .retrieve()
               .bodyToMono(ChatDomain.Response.Result.class)
               .block();


    ChatLog log = ChatLog.builder()
            .traceId(request.traceId())
            .userId(userId)
            .userQuery(request.messages().get(0).content())
            .reply(apiResult.reply())
            .build();

    chatLogRepository.save(log);

    return new ChatDomain.Response(request.traceId(), apiResult);
}
}
