package com.DOCKin.member.dto;

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
@Schema(description = "로그아웃 req dto")
public class LogOutRequestDto {
    @Schema(description = "현재 사용 중인 액세스 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "액세스 토큰은 필수입니다.")
    private String accessToken;

    @Schema(description = "현재 사용 중인 리프레시 토큰", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}
