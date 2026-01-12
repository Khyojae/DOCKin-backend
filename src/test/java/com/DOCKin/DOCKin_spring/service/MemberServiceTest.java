package com.DOCKin.DOCKin_spring.service;

import com.DOCKin.model.Member.Member;
import com.DOCKin.repository.MemberRepository;
import com.DOCKin.repository.RefreshTokenRepository;
import com.DOCKin.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 가짜 객체(Mock)를 사용하기 위한 설정
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository; // 가짜 리포지토리

    @Mock
    private RefreshTokenRepository refreshTokenRepository; // 가짜 리포지토리

    @InjectMocks
    private MemberService memberService; // 위 가짜 객체들을 주입받은 서비스

    @Test
    @DisplayName("회원 탈퇴 시 회원 정보와 리프레시 토큰이 모두 삭제되어야 한다")
    void deleteAccountTest() {
        // 1. 준비 (given)
        String userId = "testUser";
        Member member = Member.builder().userId(userId).build();

        // memberRepository가 해당 ID를 찾으면 가짜 member 객체를 반환하도록 설정
        when(memberRepository.findByUserId(userId)).thenReturn(Optional.of(member));

        // 2. 실행 (when)
        memberService.deleteAccount(userId);

        // 3. 검증 (then)
        // memberRepository의 delete 메서드가 한 번 호출되었는지 확인
        verify(memberRepository, times(1)).delete(any(Member.class));

        // refreshTokenRepository의 deleteByUserId 메서드가 해당 ID로 호출되었는지 확인
        verify(refreshTokenRepository, times(1)).deleteByUserId(userId);
    }
}