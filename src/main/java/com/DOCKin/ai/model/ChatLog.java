package com.DOCKin.ai.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "chat_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_query", columnDefinition = "TEXT")
    private String userQuery;

    @Column(name = "reply", columnDefinition = "TEXT")
    private String reply;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ChatLog(String traceId, String userId, String userQuery, String reply) {
        this.traceId = traceId;
        this.userId = userId;
        this.userQuery = userQuery;
        this.reply = reply;
        this.createdAt = LocalDateTime.now();
    }
}
