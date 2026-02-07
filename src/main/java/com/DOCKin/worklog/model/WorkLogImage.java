package com.DOCKin.worklog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_log_images")
public class WorkLogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "work_log_id")
    private Work_logs workLog;

    @Builder
    public WorkLogImage(String imageUrl, Work_logs workLog){
        this.imageUrl=imageUrl;
        this.workLog=workLog;
    }
}
