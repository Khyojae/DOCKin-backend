package com.DOCKin.controller;

import com.DOCKin.dto.LoginRequestDto;
import com.DOCKin.dto.MemberRequestDto;
import com.DOCKin.model.Member;
import com.DOCKin.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @PostMapping("login")
    public ResponseEntity<String> getMemberProfile(
            @Valid @RequestBody LoginRequestDto request
            ){
        String token = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@Valid @RequestBody MemberRequestDto member) {
        Member entity = modelMapper.map(member, Member.class);
        String id = memberService.signup(entity);
        return ResponseEntity.status(HttpStatus.OK).body(id);
        }
    }

