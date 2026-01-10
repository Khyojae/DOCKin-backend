package com.DOCKin.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

    public RefreshToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void updateToken(String newToken) {
        this.token = newToken;
    }
}
