package com.DOCKin.ai.model;

import com.DOCKin.worklog.model.Work_logs;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="translate_logs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access= AccessLevel.PROTECTED)
public class TranslateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="log_id")
    private Work_logs workLogs;

    private String userId;

    private String traceId;

    @Column(columnDefinition = "TEXT")
    private String originalText;

    @Column(columnDefinition = "TEXT")
    private String translatedText;

    private String targetLang;
}
