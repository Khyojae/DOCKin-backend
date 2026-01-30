package com.DOCKin.safetyCourse.controller;

import com.DOCKin.safetyCourse.dto.SafetyCourseCreateRequestDto;
import com.DOCKin.safetyCourse.dto.SafetyCourseResponseDto;
import com.DOCKin.safetyCourse.dto.SafetyCourseUpdateRequestDto;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.safetyCourse.service.SafetyCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name="관리자용 안전교육 관리", description="안전교육을 관리할 수 있는 api")
@Slf4j
@RestController
@RequestMapping("/api/safety/admin")
@RequiredArgsConstructor
public class SafetyAdminController {

    private final SafetyCourseService safetyCourseService;

    @Operation(summary="교육 자료 등록",description = "교육 자료를 등록할 수 있음")
    @PostMapping("/courses")
    public ResponseEntity<SafetyCourseResponseDto> createCourse(@Valid @RequestBody SafetyCourseCreateRequestDto dto,
                                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String creatorId = customUserDetails.getMember().getUserId();
        SafetyCourseResponseDto safetyCourse = safetyCourseService.createSafetyCourseResponse(dto,creatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(safetyCourse);
    }

    @Operation(summary="전체 교육 자료 조회",description = "전체 교육 자료를 조회할 수 있음")
    @GetMapping("/courses")
    public ResponseEntity<Page<SafetyCourseResponseDto>> getAllCourses(@PageableDefault(size = 20,
            sort = "courseId",direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(safetyCourseService.readSafetyCourse(pageable));
    }


    @Operation(summary="특정 작성자가 쓴 교육 상세 조회",description = "특정 작성자가 쓴 교육 자료를 조회할 수 있음")
    @GetMapping("/courses/user/{userId}")
    public ResponseEntity<Page<SafetyCourseResponseDto>> getCourseDetail(
                                                                   @PathVariable String userId,
                                                                   @PageableDefault(size = 20,
                                                                           sort = "courseId",
                                                                           direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(safetyCourseService.searchOtherSafetyCourse(userId,pageable));
    }

    @Operation(summary="교육 자료 수정",description = "특정 교육 자료를 수정할 수 있음")
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponseDto> updateCourse(@PathVariable Integer courseId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                               @Valid @RequestBody SafetyCourseUpdateRequestDto dto) {
        String userId = customUserDetails.getMember().getUserId();
        SafetyCourseResponseDto safetyCourse = safetyCourseService.reviseSafetyCourse(dto,userId,courseId);
        return ResponseEntity.ok(safetyCourse);
    }

    @Operation(summary="특정 교육 자료 삭제",description = "특정 교육자료를 삭제할 수 있음")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @PathVariable Integer courseId) {
        String userId = customUserDetails.getMember().getUserId();
        safetyCourseService.deleteSafetyCourse(userId,courseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "키워드로 검색하기",description = "키워드로 제목이나 내용을 검색할 수 있음")
    @GetMapping("/courses/search")
    public ResponseEntity<Page<SafetyCourseResponseDto>> searchByKeyword(String keyword,
                                                                         @PageableDefault(size= 20,
                                                                         sort="courseId",
                                                                         direction=Sort.Direction.DESC)Pageable pageable){
        return ResponseEntity.ok(safetyCourseService.searchSafetyCourse(keyword,pageable));

    }

}
