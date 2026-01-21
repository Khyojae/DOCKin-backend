package com.DOCKin.controller;

import com.DOCKin.dto.SafetyCourse.SafetyCourseCreateRequestDto;
import com.DOCKin.dto.SafetyCourse.SafetyCourseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="관리자용 안전교육 관리", description="안전교육을 관리할 수 있는 api")
@Slf4j
@RestController
@RequestMapping("/api/safety/admin")
@RequiredArgsConstructor
public class SafetyAdminController {

    @Operation(summary="교육 자료 등록",description = "교육 자료를 등록할 수 있음")
    @PostMapping("/courses")
    public ResponseEntity<SafetyCourseResponseDto> createCourse(@RequestBody SafetyCourseCreateRequestDto safetyCourseCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SafetyCourseResponseDto());
    }

    @Operation(summary="전체 교육 자료 조회",description = "전체 교육 자료를 조회할 수 있음")
    @GetMapping("/courses")
    public ResponseEntity<List<SafetyCourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(null);
    }


    @Operation(summary="특정 교육 상세 조회",description = "특정 교육 자료를 조회할 수 있음")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponseDto> getCourseDetail(@PathVariable Integer courseId) {
        return ResponseEntity.ok(new SafetyCourseResponseDto());
    }

    @Operation(summary="교육 자료 수정",description = "특정 교육 자료를 수정할 수 있음")
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponseDto> updateCourse(@PathVariable Integer courseId,
                                                                @RequestBody SafetyCourseCreateRequestDto request) {
        return ResponseEntity.ok(new SafetyCourseResponseDto());
    }

    @Operation(summary="특정 교육 자료 삭제",description = "특정 교육자료를 삭제할 수 있음")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        return ResponseEntity.noContent().build();
    }

}
