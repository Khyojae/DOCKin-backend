package com.DOCKin.controller;

import com.DOCKin.dto.Member.LoginRequestDto;
import com.DOCKin.dto.Member.LoginResponseDto;
import com.DOCKin.dto.Member.MemberRequestDto;
import com.DOCKin.repository.RefreshTokenRepository;
import com.DOCKin.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="인증/인가", description="로그인/회원가입")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Operation(summary="로그인",description = "로그인을 할 수 있음")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> getMemberProfile(
            @Valid @RequestBody LoginRequestDto request){
        LoginResponseDto response = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary="회원가입",description = "회원가입을 할 수 있음")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody MemberRequestDto dto) {
        //Member entity = modelMapper.map(member, Member.class);
        String id = memberService.signup(dto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
        }

    @Operation(summary="회원탈퇴", description = "회원탈퇴를 할 수 있음")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("userId") String userId){
        memberService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }

    }

