package com.DOCKin.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false, length = 512)
    private String token;

}
