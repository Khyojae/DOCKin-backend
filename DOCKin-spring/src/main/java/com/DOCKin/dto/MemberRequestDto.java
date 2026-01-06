package com.DOCKin.dto;

import com.DOCKin.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//회원가입용 Dto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank(message="사원번호는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message="이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message="비밀번호는 필수 입력 값입니다.")
    private String password;

    private UserRole role;
}
