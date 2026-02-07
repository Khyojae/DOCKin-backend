package com.DOCKin.ai.service;

import com.DOCKin.ai.dto.ChatDomain;
import com.DOCKin.ai.dto.TranslateDomain;
import com.DOCKin.ai.dto.onlineTranslateDomain;
import com.DOCKin.ai.model.ChatLog;
import com.DOCKin.ai.model.TranslateLog;
import com.DOCKin.ai.repository.ChatLogRepository;
import com.DOCKin.ai.repository.TranslateRepository;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.worklog.model.Work_logs;
import com.DOCKin.worklog.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class fastApiService {
    private final ChatLogRepository chatLogRepository;
    private final WebClient fastApiWebClient;
    private final Work_logsRepository workLogsRepository;
    private final TranslateRepository translateRepository;
    private final SttService sttService;

    //1. 그냥 번역 api
    public Mono<TranslateDomain.Response> translateForRealTime(TranslateDomain.ApiRequest request){
        return fastApiWebClient.post()
                .uri("/api/translate")
                .bodyValue(request)
                .retrieve()
                .onStatus(status->status.isError(), clientResponse->
                        Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR)))
                .bodyToMono(TranslateDomain.Response.class);
    }

    //1. 실시간 번역 (stt -> 번역)
    public Mono<onlineTranslateDomain.RtTranslateResponse> realtimeTranslate(
            MultipartFile file, String source, String target, String traceId, String token){

        return sttService.processStt(file,traceId,token,source)
                .flatMap(sttResponse->{
                    String recognizedText = sttResponse.text();

                    TranslateDomain.ApiRequest apiRequest = new TranslateDomain.ApiRequest(
                            recognizedText, source, target, traceId
                    );

                    return translateForRealTime(apiRequest)
                            .map(transResponse -> {
                                onlineTranslateDomain.TranslationResult result =
                                        new onlineTranslateDomain.TranslationResult(
                                                recognizedText,
                                                transResponse.translated()
                                        );

                                return new onlineTranslateDomain.RtTranslateResponse(traceId, result);
                            });
                });
    }

    // 2. 챗봇 통신 (순수하게 결과만 리턴)
    public Mono<ChatDomain.Response> chatBotFromSpringToFastApi(ChatDomain.Request request, String userId) {
        return fastApiWebClient.post()
                .uri("/api/chatbot")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        Mono.error(new BusinessException(ErrorCode.CHATBOT_NOT_WORK)))
                .bodyToMono(ChatDomain.Response.Result.class)
                .map(apiResult -> new ChatDomain.Response(request.traceId(), apiResult));
    }

    //  챗봇 로그 저장 (컨트롤러에서 호출)
    @Transactional
    public void saveChatLog(ChatDomain.Request request, ChatDomain.Response response, String userId) {
        ChatLog log = ChatLog.builder()
                .traceId(request.traceId())
                .userId(userId)
                .userQuery(request.messages().get(0).content())
                .reply(response.result().reply())
                .build();
        chatLogRepository.save(log);
    }

    // 3. 작업일지 번역 (통신만)
    public Mono<TranslateDomain.Response> translateLogFromSpringToFastApi(TranslateDomain.Request request) {
        return fastApiWebClient.post()
                .uri("/api/translate")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("### FastApi 400 Error Detail: {}", errorBody);
                            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
                        })
                )
                .onStatus(status -> status.is5xxServerError(), clientResponse ->
                        Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR)))
                .bodyToMono(TranslateDomain.Response.class);
    }

    // 작업일지 번역된거 저장
    @Transactional
    public TranslateDomain.Response saveTranslateLog(Long logId, TranslateDomain.Request request, String userId) {
        // 1. 원본 로그 조회
        Work_logs workLogEntity = workLogsRepository.findById(logId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOG_NOT_FOUND));

        // 2. 제목 번역용 요청 생성
        TranslateDomain.ApiRequest titleReq = new TranslateDomain.ApiRequest(
                workLogEntity.getTitle(), "ko", request.target(), request.traceId());

        // 3. 본문 번역용 요청 생성
        TranslateDomain.ApiRequest contentReq = new TranslateDomain.ApiRequest(
                workLogEntity.getLogText(), "ko", request.target(), request.traceId());

        // 4. 각각 통신 (FastAPI 응답의 'translated' 필드를 맵에서 꺼냄)
        var titleMap = fastApiWebClient.post()
                .uri("/api/translate")
                .bodyValue(titleReq)
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .block();

        var contentMap = fastApiWebClient.post()
                .uri("/api/translate")
                .bodyValue(contentReq)
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .block();

        if (titleMap == null || contentMap == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // FastAPI 응답 필드명인 "translated"로 데이터 추출
        String transTitle = ((String) titleMap.get("translated")).trim();
        String transContent = ((String) contentMap.get("translated")).trim();
        String modelName = (String) titleMap.get("model");

        // 5. DB 저장 (원문/번역본 각각 저장)
        TranslateLog translateLog = TranslateLog.builder()
                .traceId(request.traceId())
                .workLogs(workLogEntity)
                .userId(userId)
                .originalTitle(workLogEntity.getTitle())
                .translatedTitle(transTitle)
                .originalText(workLogEntity.getLogText())
                .translatedText(transContent)
                .targetLang(request.target())
                .build();

        translateRepository.save(translateLog);

        // 6. Response DTO 구조에 맞춰서 리턴
        return new TranslateDomain.Response(
                transTitle,
                transContent,
                modelName,
                request.traceId()
        );
    }
}