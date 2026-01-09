package com.DOCKin.service;

import com.DOCKin.model.Member;
import com.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 1. DB에서 사용자 조회
        return memberRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사원번호를 가진 사용자를 찾을 수 없습니다: " + userId));
    }

    // 2. DB에서 가져온 Member 객체를 스프링 시큐리티의 UserDetails 객체로 변환
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword()) // DB에 암호화되어 저장된 비밀번호
                .roles(member.getRole().name()) // ROLE_USER 등 권한
                .build();
    }
}
