package com.DOCKin.ai.controller;

import com.DOCKin.ai.dto.ChatDomain;
import com.DOCKin.ai.dto.TranslateDomain;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.ai.service.fastApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Fast api 통신용")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final fastApiService chatBotService;

    @Operation(summary = "챗봇",description = "fast api에서 챗봇 json을 받는다")
    @PostMapping("/chatbot")
    public ChatDomain.Response chatBot(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @Valid @RequestBody ChatDomain.Request request){

        if (customUserDetails == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String userId = customUserDetails.getMember().getUserId();
        return chatBotService.chatBotFromSpringToFastApi(request,userId).block();
    }

    @Operation(summary="작업일지 번역",description = "fast api에서 작업일지 번역을 받는다")
    @PostMapping("/translate/{logId}")
    public TranslateDomain.Response translateWorklog(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @Valid @RequestBody TranslateDomain.Request request,
                                                           @PathVariable Long logId){
        String userId = customUserDetails.getMember().getUserId();
        return  chatBotService.translateLogFromSpringToFastApi(logId,request,userId).block();
    }



}
