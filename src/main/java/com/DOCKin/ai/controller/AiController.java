package com.DOCKin.ai.controller;

import com.DOCKin.ai.dto.ChatDomain;
import com.DOCKin.ai.dto.TranslateDomain;
import com.DOCKin.ai.dto.onlineTranslateDomain;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.ai.service.fastApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Tag(name = "Fast api 통신용")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final fastApiService fastApiService;

    @Operation(summary= "stt 실시간 번역",description = "실시간 번역을 해준다")
    @PostMapping(value = "/rt-translate", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<onlineTranslateDomain.RtTranslateResponse>> rtTranslate(
            @RequestPart("file")MultipartFile file,
            @RequestPart("source") String source,
            @RequestPart("target") String target,
            @RequestPart("traceId") String traceId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
            ){
return fastApiService.realtimeTranslate(file, source, target, traceId, token)
        .map(response->ResponseEntity.ok(response));
    }

    @Operation(summary = "챗봇", description = "fast api에서 챗봇 json을 받는다")
    @PostMapping("/chatbot")
    public ChatDomain.Response chatBot(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       @Valid @RequestBody ChatDomain.Request request) {

        if (customUserDetails == null) throw new BusinessException(ErrorCode.UNAUTHORIZED);
        String userId = customUserDetails.getMember().getUserId();

        // 1. 통신 결과 대기 (.block)
        ChatDomain.Response response = fastApiService.chatBotFromSpringToFastApi(request, userId).block();

        // 2. 현재 스레드(인증정보 있음)에서 DB 저장 호출
        fastApiService.saveChatLog(request, response, userId);

        return response;
    }

    @Operation(summary = "작업일지 번역", description = "fast api에서 작업일지 번역을 받는다")
    @PostMapping("/translate/{logId}")
    public TranslateDomain.Response translateWorklog(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @Valid @RequestBody TranslateDomain.Request request,
                                                     @PathVariable Long logId) {

        if (customUserDetails == null) throw new BusinessException(ErrorCode.UNAUTHORIZED);
        String userId = customUserDetails.getMember().getUserId();

        TranslateDomain.Response response = fastApiService.saveTranslateLog(logId,request,userId);

        return response;
    }
}