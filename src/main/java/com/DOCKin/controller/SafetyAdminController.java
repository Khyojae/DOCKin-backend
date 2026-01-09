package com.DOCKin.controller;

import com.DOCKin.dto.SafetyCourseCreateRequest;
import com.DOCKin.dto.SafetyCourseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="관리자용 안전교육 관리", description="안전교육을 관리할 수 있는 api")
@RestController
@RequestMapping("/api/safety/admin")
@RequiredArgsConstructor
public class SafetyAdminController {

    @Operation(summary="전체 교육 자료 조회", description = "전체 교육 자료를 조회할 수 있음")
    @GetMapping("/enrollments")
    public ResponseEntity<List<SafetyCourseResponse>> getAllEnrollments(){
        return ResponseEntity.ok(null);
    }

    @Operation(summary="교육 자료 등록",description = "교육 자료를 등록할 수 있음")
    @PostMapping("/courses")
    public ResponseEntity<SafetyCourseResponse> createCourse(@RequestBody SafetyCourseCreateRequest safetyCourseCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SafetyCourseResponse());
    }

    @Operation(summary="전체 교육 자료 조회",description = "전체 교육 자료를 조회할 수 있음")
    @GetMapping("/courses")
    public ResponseEntity<List<SafetyCourseResponse>> getAllCourses() {
        return ResponseEntity.ok(null);
    }


    @Operation(summary="특정 교육 상세 조회",description = "특정 교육 자료를 조회할 수 있음")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponse> getCourseDetail(@PathVariable Integer courseId) {
        return ResponseEntity.ok(new SafetyCourseResponse());
    }

    @Operation(summary="교육 자료 수정",description = "특정 교육 자료를 수정할 수 있음")
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponse> updateCourse(@PathVariable Integer courseId,
                                          @RequestBody SafetyCourseCreateRequest request) {
        return ResponseEntity.ok(new SafetyCourseResponse());
    }

    @Operation(summary="특정 교육 자료 삭제",description = "특정 교육자료를 삭제할 수 있음")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer courseId) {
        return ResponseEntity.noContent().build();
    }

}
