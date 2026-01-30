package com.DOCKin.member.repository;

import com.DOCKin.member.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserId(String userId);
    void deleteByToken(String refreshToken);
}
