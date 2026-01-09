package com.DOCKin.dto;

import lombok.Getter;

@Getter
public class SafetyCourseCreateRequest {
    private Integer courseId;
    private String title;
    private String description;
    private String videoUrl;
    private Integer durationMinutes;
}
