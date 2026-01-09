package com.DOCKin.dto;

import com.DOCKin.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDto extends MemberDto{
    private String userId;
    private String name;
    private String password;
    private UserRole role;
}
