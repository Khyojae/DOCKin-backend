package com.DOCKin.member.dto;

import com.DOCKin.member.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserInfoDto{
    private String userId;
    private String name;
    private String password;
    private UserRole role;
}
