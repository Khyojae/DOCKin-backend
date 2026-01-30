package com.DOCKin.member.dto;

import com.DOCKin.member.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "로그인 res dto (토큰 포함)")
public class LoginResponseDto {
    @Schema(description = "액세스 토큰 (API 요청 시 Authorization 헤더에 Bearer로 포함)",
            example = "eyJhbGciOiJIUzI1NiJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String accessToken;

    @Schema(description = "리프레시 토큰 (액세스 토큰 만료 시 재발급용)",
            example = "eyJhbGciOiJIUzI1NiJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    @Schema(description = "이름",
            example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="이름은 필수 입력 값입니다.")
    private String name;

    @Schema(description = "사용자 권한", example = "USER")
    private UserRole role;

}
