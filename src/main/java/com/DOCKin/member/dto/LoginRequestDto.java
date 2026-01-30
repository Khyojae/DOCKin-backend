package com.DOCKin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 req dto")
public class LoginRequestDto {
    @Schema(description = "사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="사원번호는 필수 입력 값입니다.")
    private String userId;

    @Schema(description = "비밀번호",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="비밀번호는 필수 입력 값입니다.")
    private String password;
}
