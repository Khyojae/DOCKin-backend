package com.DOCKin.worklog.controller;

import com.DOCKin.worklog.dto.CommentCreateRequestDto;
import com.DOCKin.worklog.dto.CommentResponseDto;
import com.DOCKin.worklog.dto.CommentUpdateRequestDto;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.worklog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="작업일지 댓글 기능",description = "작업일지에 댓글을 달 수 있음")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs/{logId}/comments")
public class WorkLogCommentController {
    private final CommentService commentService;

    @Operation(summary = "관리자 댓글 작성",description = "관리자가 근로자 작업일지 피드백을 해줌")
    @PostMapping
    public ResponseEntity<CommentResponseDto> postComment(@AuthenticationPrincipal CustomUserDetails customUserDetails
    , @Valid @RequestBody CommentCreateRequestDto dto,@PathVariable Long logId){
        String userId =customUserDetails.getMember().getUserId();
        CommentResponseDto comment =commentService.createComment(logId,userId,dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @Operation(summary="관리자 댓글 수정",description = "관리자가 자기가 쓴 댓글을 수정함")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> putComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @Valid @RequestBody CommentUpdateRequestDto dto,
                                                            @PathVariable Long logId,
                                                         @PathVariable Long commentId){
        String userId =customUserDetails.getMember().getUserId();
        CommentResponseDto comment = commentService.updateComment(logId,userId,commentId,dto);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary="관리자 댓글 조회",description = "관리자가 쓴 댓글을 볼 수 있음")
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @PathVariable Long logId){
        List<CommentResponseDto> comment = commentService.readComment(logId);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary ="관리자 댓글 삭제",description = "관리자가 쓴 댓글을 삭제할 수 있음")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @PathVariable Long logId,
                                              @PathVariable Long commentId){
        String userId = customUserDetails.getMember().getUserId();
        commentService.deleteComment(logId,commentId,userId);
        return ResponseEntity.noContent().build();
    }
}
