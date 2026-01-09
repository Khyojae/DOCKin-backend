package com.DOCKin.service;

import com.DOCKin.dto.CustomUserInfoDto;
import com.DOCKin.dto.LoginRequestDto;
import com.DOCKin.dto.MemberRequestDto;
import com.DOCKin.global.error.ValidateMemberException;
import com.DOCKin.global.security.jwt.JwtUtil;
import com.DOCKin.model.Member;
import com.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Transactional
    public String login(LoginRequestDto dto){
        String userId = dto.getUserId();
        String password = dto.getPassword();
        Optional<Member> member = memberRepository.findByUserId(dto.getUserId());
    if(member.isEmpty()){
        throw new UsernameNotFoundException("사원번호가 존재하지 않습니다.");
    }
    if(!encoder.matches(password,member.get().getPassword())){
        throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
    }
        CustomUserInfoDto info = modelMapper.map(member,CustomUserInfoDto.class);
    return jwtUtil.createAccessToken(info);
    }

    @Transactional
    public String signup(MemberRequestDto dto) {
        if(memberRepository.existsById(dto.getUserId())) {
            throw new ValidateMemberException("이미 존재하는 사번입니다: " + dto.getUserId());
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
}