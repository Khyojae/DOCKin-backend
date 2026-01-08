package com.DOCKin.dto;

import com.DOCKin.model.Member;
import com.DOCKin.model.UserRole;
import com.DOCKin.repository.MemberRepository;
import com.DOCKin.service.MemberService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private String language_code;

    private Boolean tts_enabled;

    private String shipYardArea;
}
