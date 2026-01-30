package com.DOCKin.member.service;

import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.jwt.JwtBlacklist;
import com.DOCKin.global.security.jwt.JwtUtil;
import com.DOCKin.member.dto.*;
import com.DOCKin.member.model.Member;
import com.DOCKin.member.model.RefreshToken;
import com.DOCKin.member.model.UserRole;
import com.DOCKin.member.repository.MemberRepository;
import com.DOCKin.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtBlacklist jwtBlacklist;
    //로그인 로직
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto){
        Member member = memberRepository.findByUserId(dto.getUserId()).
                orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())){
        throw new BusinessException(ErrorCode.LOGIN_INPUT_INVALID);
    }
        CustomUserInfoDto info = CustomUserInfoDto.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .role(member.getRole())
                .build();

    String accessToken=jwtUtil.createAccessToken(info);
    String refreshToken=jwtUtil.createRefreshToken(info);
    String name = member.getName();
        UserRole role = member.getRole();

        RefreshToken refreshTokenEntity= RefreshToken.builder()
                .userId(member.getUserId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    return new LoginResponseDto(accessToken,refreshToken,name,role);
    }

    //로그아웃 로직
    @Transactional
    public void logout(LogOutRequestDto dto){
        refreshTokenRepository.deleteByToken(dto.getRefreshToken());
        String token = dto.getAccessToken();
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        long expiration = jwtUtil.getExpiration(token);
        jwtBlacklist.add(token,expiration);
    }

    //회원가입 로직
    @Transactional
    public String signup(MemberRequestDto dto) {
        if(memberRepository.existsById(dto.getUserId())) {
            throw new BusinessException(ErrorCode.USERID_DUPLICATION);
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = Member.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .password(encodedPassword)
                .role(dto.getRole())
                .language_code(dto.getLanguage_code())
                .tts_enabled(dto.getTts_enabled())
                .shipYardArea(dto.getShipYardArea())
                .build();
        memberRepository.save(member);
        return member.getUserId();
    }

    //회원탈퇴 로직
    @Transactional
    public void deleteAccount(String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
        memberRepository.delete(member);
        refreshTokenRepository.deleteByUserId(userId);
    }
}