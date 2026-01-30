package com.DOCKin.safetyCourse.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "safety_courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SafetyCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "material_url")
    private String materialUrl;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "DATETIME", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public SafetyCourse(String title, String description, String videoUrl,String materialUrl, Integer durationMinutes, String createdBy) {
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.materialUrl = materialUrl;
        this.durationMinutes = durationMinutes;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }


}