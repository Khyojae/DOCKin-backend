package com.DOCKin.global.file;

import com.DOCKin.worklog.model.Work_logs;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="Log_images")
public class LogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgaeId;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    private Work_logs workLog;

    @Builder
    public LogImage(String imageUrl, Work_logs workLog){
        this.imageUrl = imageUrl;
        this.workLog = workLog;
    }
}
