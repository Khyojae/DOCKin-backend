package com.DOCKin.worklog.controller;

import com.DOCKin.ai.dto.SttDomain;
import com.DOCKin.ai.service.SttService;
import com.DOCKin.worklog.dto.WorkLogsCreateRequestDto;
import com.DOCKin.worklog.dto.WorkLogsUpdateRequestDto;
import com.DOCKin.worklog.dto.Work_logsDto;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.worklog.service.WorkLogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;


@Tag(name="작업일지 CRUD",description = "작업일지 CRUD가 가능함")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs")
public class WorkLogsController {
    private final WorkLogsService workLogsService;

    @Operation(summary="특정 작업자 작업일지 생성(사진 포함)",description = "특정 작업자의 작업일지를 생성해줌")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Work_logsDto> createWorkLog(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                      @Valid @RequestPart(value="requestDto") WorkLogsCreateRequestDto requestDto,
                                                      @RequestPart(value="images", required=false)List<MultipartFile> images
                                                      ){
        String userId = customUserDetails.getMember().getUserId();
        Work_logsDto response =  workLogsService.createWorklog(userId,requestDto,images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary="Stt용 특정 작업자 작업일지 생성",description = "특정 작업자의 작업일지를 생성해줌")
    @PostMapping(value= "/stt",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Work_logsDto> createWorkLog( @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestPart(value="request") @Valid WorkLogsCreateRequestDto requestDto,
                                                       @RequestPart(value="file",required = false) MultipartFile file,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @RequestPart(value="images",required = false) List<MultipartFile> images
    ){
        String userId = customUserDetails.getMember().getUserId();
      Work_logsDto response =  workLogsService.createSttWorklog(userId,requestDto,file,token,images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary="전체 작업일지 조회",description = "전체 작업자의 작업일지를 조회해줌")
    @GetMapping
    public ResponseEntity<Page<Work_logsDto>> getWorkLog(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 20,direction = Sort.Direction.DESC)Pageable pageable
            ){
        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(workLogsService.readWorklog(userId,pageable));
    }

    @Operation(summary="특정 작업자 작업일지 조회",description = "특정 작업자의 작업일지를 조회해줌")
    @GetMapping("/others/{targetUserId}")
    public ResponseEntity<Page<Work_logsDto>> getMyWorkLog(@PathVariable String targetUserId,
                                                           @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                           @PageableDefault(size=20, direction = Sort.Direction.DESC)Pageable pageable){
        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(workLogsService.readOtherWorklog(userId,targetUserId,pageable));
    }

    @Operation(summary="특정 작업자 작업일지 수정",description = "특정 작업자의 작업일지를 수정해줌")
    @PutMapping("/{logId}")
    public ResponseEntity<Work_logsDto> PutMyWorkLog(@PathVariable Long logId,
                                                     @Valid @RequestPart(value = "requestDto") WorkLogsUpdateRequestDto request,
                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestPart(value="images", required = false) List<MultipartFile> images){
        String userId = customUserDetails.getMember().getUserId();
        Work_logsDto response = workLogsService.updateWorklog(userId,logId,request,images);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "키워드로 게시물 검색", description = "키워드로 게시물 검색이 가능함")
    @GetMapping("/search")
    public ResponseEntity<Page<Work_logsDto>> searchByKeyword( @PageableDefault(size=20,sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable,
                                                              String keyword){
        Page<Work_logsDto> workLogsDtos = workLogsService.searchByKeyword(keyword,pageable);
        return ResponseEntity.ok(workLogsDtos);
    }

    @Operation(summary="특정 작업자 작업일지 삭제",description = "특정 작업자의 작업일지를 삭제해줌")
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> DeleteMyWorkLog(@PathVariable Long logId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userId = customUserDetails.getMember().getUserId();
        workLogsService.deleteWorklog(userId,logId);
        return ResponseEntity.noContent().build();
    }
}
