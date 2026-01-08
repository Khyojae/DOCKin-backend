package com.DOCKin.controller;

import com.DOCKin.dto.LoginRequestDto;
import com.DOCKin.dto.MemberRequestDto;
import com.DOCKin.model.Member;
import com.DOCKin.repository.MemberRepository;
import com.DOCKin.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="인증/인가", description="로그인/회원가입")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Operation(summary="로그인",description = "로그인을 할 수 있음")
    @PostMapping("/login")
    public ResponseEntity<String> getMemberProfile(
            @Valid @RequestBody LoginRequestDto request){
        String token = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Operation(summary="회원가입",description = "회원가입을 할 수 있음")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody MemberRequestDto dto) {
        //Member entity = modelMapper.map(member, Member.class);
        String id = memberService.signup(dto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
        }

    }

