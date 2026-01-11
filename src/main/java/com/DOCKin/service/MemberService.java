package com.DOCKin.service;

import com.DOCKin.dto.Member.CustomUserInfoDto;
import com.DOCKin.dto.Member.LoginRequestDto;
import com.DOCKin.dto.Member.LoginResponseDto;
import com.DOCKin.dto.Member.MemberRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.global.security.jwt.JwtUtil;
import com.DOCKin.model.Member.Member;
import com.DOCKin.model.Member.RefreshToken;
import com.DOCKin.repository.MemberRepository;
import com.DOCKin.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    //로그인 로직
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto){
        Member member = memberRepository.findByUserId(dto.getUserId()).
                orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

    if(!encoder.matches(dto.getPassword(), member.getPassword())){
        throw new BusinessException(ErrorCode.LOGIN_INPUT_INVALID);
    }
        CustomUserInfoDto info = modelMapper.map(member,CustomUserInfoDto.class);

    String accessToken=jwtUtil.createAccessToken(info);
    String refreshToken=jwtUtil.createRefreshToken(info);

        RefreshToken refreshTokenEntity= RefreshToken.builder()
                .userId(member.getUserId())
                .token(refreshToken)
                .build();

    return new LoginResponseDto(accessToken,refreshToken);
    }

    //회원가입 로직
    @Transactional
    public String signup(MemberRequestDto dto) {
        if(memberRepository.existsById(dto.getUserId())) {
            throw new BusinessException(ErrorCode.USERID_DUPLICATION);
        }
        Member member = Member.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .password(encoder.encode(dto.getPassword()))
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