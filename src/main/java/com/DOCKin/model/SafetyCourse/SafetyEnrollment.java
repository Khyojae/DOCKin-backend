package com.DOCKin.model.SafetyCourse;

import com.DOCKin.dto.SafetyCourse.CompletedLabel;
import com.DOCKin.model.Member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "safety_enrollments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SafetyEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private SafetyCourse courseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private CompletedLabel status = CompletedLabel.UNWATCHED;

    @Column(name = "completion_date")
    private LocalDateTime completion_date;

    @CreationTimestamp
    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;


    public void updateStatus(CompletedLabel status){
        this.status = status;
        if (status == CompletedLabel.WATCHED) {
            this.completion_date = LocalDateTime.now();
        }
    }
}