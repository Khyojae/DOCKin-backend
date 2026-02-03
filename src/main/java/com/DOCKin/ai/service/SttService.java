package com.DOCKin.ai.service;

import com.DOCKin.ai.dto.SttDomain;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.util.AudioConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class SttService {
    private final AudioConverter audioConverter;
    private final WebClient fastApiWebClient;

    public Mono<SttDomain.Response> processStt(MultipartFile file,String traceId, String token,String lang){
        try{
            File wavFile = audioConverter.convertToWav(file);

            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file",new FileSystemResource(wavFile))
                    .filename("speech.wav")
                    .contentType(MediaType.parseMediaType("audio/wav"));

            if(lang!=null){
                builder.part("lang",lang);
            }
            builder.part("traceId",traceId);

            return fastApiWebClient.post()
                    .uri("/api/worklogs/stt")
                    .header(HttpHeaders.AUTHORIZATION,token)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(SttDomain.Response.class)
                    .doFinally(signalType -> {
                        if(wavFile.exists()) wavFile.delete();
                    });

        } catch (Exception e){
            log.error("STT 처리 중 오류 발생:{}",e.getMessage());
            return Mono.error(new BusinessException(ErrorCode.STT_CONVERSION_ERROR));
        }
    }
}
