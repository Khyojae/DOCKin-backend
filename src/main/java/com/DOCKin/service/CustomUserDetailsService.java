package com.DOCKin.service;

import com.DOCKin.dto.Member.CustomUserInfoDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.model.Member.Member;
import com.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//로그인 인증 로직
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByUserId(userId)
                .map(member->{
                    CustomUserInfoDto infoDto = new CustomUserInfoDto(
                            member.getUserId(),
                            member.getPassword(),
                            member.getName(),
                            member.getRole()
                    );
                    return new CustomUserDetails(infoDto);
                })
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

}
