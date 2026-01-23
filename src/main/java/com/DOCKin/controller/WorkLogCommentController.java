package com.DOCKin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="작업일지 댓글 기능",description = "작업일지에 댓글을 달 수 있음")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-logs/{logId}/comments")
public class WorkLogCommentController {

    @Operation(summary = "관리자 댓글 작성")
    @PostMapping
    public ResponseEntity
}
