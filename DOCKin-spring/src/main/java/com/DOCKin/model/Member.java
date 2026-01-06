package com.DOCKin.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="users")
public class Member {
    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(nullable=false, length = 10)
    private String name;

    @Column(nullable=false, length = 256)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole role;

    @Column(nullable=false)
    private String language_code;

    @Column(nullable=false)
    private Boolean tts_enabled;

    @CreationTimestamp
    @Column(updatable=false, nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false, length = 100)
    private String shipYardArea;

    public void updateSettings(String language_code, Boolean tts_enabled) {
        this.language_code = language_code;
        this.tts_enabled = tts_enabled;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
