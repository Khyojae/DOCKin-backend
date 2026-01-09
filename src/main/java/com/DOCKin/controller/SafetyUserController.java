package com.DOCKin.controller;

import com.DOCKin.dto.SafetyCourseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="사용자 안전교육",description = "사용자 안전교육 담당 api")
@RestController
@RequestMapping("/api/safety/user")
@RequiredArgsConstructor
public class SafetyUserController {

    @Operation(summary="미이수 영상 조회", description = "사용자의 미이수 영상을 조회해줌")
    @GetMapping("/training/uncompleted")
    public ResponseEntity<List<SafetyCourseResponse>> getUncompletedVideos() {
        return ResponseEntity.ok(null);
    }

    @Operation(summary="영상 조회 완료", description="사용자가 영상 조회를 완료했는지 확인해줌")
    @PostMapping("/training/complete/{videoId}")
    public ResponseEntity<Void> completeCourse(@PathVariable("videoId") Long videoId) {
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="전체 교육 조회", description = "전체 교육을 조회해줌")
    @GetMapping("/courses")
    public ResponseEntity<List<SafetyCourseResponse>> getAllCoursesForUser() {
        return ResponseEntity.ok(null);
    }

    @Operation(summary="특정 교육 조회", description = "특정 교육을 조회해줌")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<SafetyCourseResponse> getCourseDetailForUser(@PathVariable("courseId") Integer courseId) {
        return ResponseEntity.ok(new SafetyCourseResponse());
    }
}
