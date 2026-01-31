package com.DOCKin.ai.service;

import com.DOCKin.ai.dto.ChatDomain;
import com.DOCKin.ai.dto.TranslateDomain;
import com.DOCKin.ai.model.ChatLog;
import com.DOCKin.ai.model.TranslateLog;
import com.DOCKin.ai.repository.ChatLogRepository;
import com.DOCKin.ai.repository.TranslateRepository;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.worklog.model.Work_logs;
import com.DOCKin.worklog.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class fastApiService {
    private final ChatLogRepository chatLogRepository;
    private final WebClient fastApiWebClient;
    private final Work_logsRepository workLogsRepository;
    private final TranslateRepository translateRepository;

    @Transactional
    public Mono<ChatDomain.Response> chatBotFromSpringToFastApi(ChatDomain.Request request, String userId) {
        return fastApiWebClient.post()
                .uri("/api/chatbot")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        Mono.error(new BusinessException(ErrorCode.CHATBOT_NOT_WORK)))
                .bodyToMono(ChatDomain.Response.Result.class)
                .flatMap(apiResult ->
                        Mono.fromCallable(() -> {
                                    ChatLog log = ChatLog.builder()
                                            .traceId(request.traceId())
                                            .userId(userId)
                                            .userQuery(request.messages().get(0).content())
                                            .reply(apiResult.reply())
                                            .build();
                                    return chatLogRepository.save(log);
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .then(Mono.just(new ChatDomain.Response(request.traceId(), apiResult)))
                );
    }

    @Transactional
    public Mono<TranslateDomain.Response> translateLogFromSpringToFastApi(Long logId, TranslateDomain.Request request,String userId){

        //Mono를 이용한 DB에서의 작업찾기 서칭
        return Mono.fromCallable(() -> workLogsRepository.findById(logId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND)))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(workLogEntity -> {


                    return fastApiWebClient.post()
                            .uri("/api/translate")
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(TranslateDomain.Response.class)
                            .flatMap(apiResult -> {

                                TranslateLog translateLog = TranslateLog.builder()
                                        .traceId(request.traceId())
                                        .workLogs(workLogEntity)
                                        .originalText(request.text())
                                        .userId(userId)
                                        .translatedText(apiResult.result().translated())
                                        .targetLang(request.target())
                                        .build();

                                //DB에 번역본 저장 후 결과 반환
                                return Mono.fromCallable(() -> translateRepository.save(translateLog))
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .thenReturn(apiResult);
                            });
                });

    }
}
