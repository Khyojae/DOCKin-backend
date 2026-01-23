package com.DOCKin.dto.Member;

import com.DOCKin.model.Member.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//회원가입용 Dto
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 req dto")
public class MemberRequestDto {
    @Schema(description = "사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="사원번호는 필수 입력 값입니다.")
    private String userId;

    @Schema(description = "이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="이름은 필수 입력 값입니다.")
    private String name;

    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="비밀번호는 필수 입력 값입니다.")
    private String password;

    @Schema(description = "사용자 권한", example = "USER",requiredMode = Schema.RequiredMode.REQUIRED)
    private UserRole role;

    @Schema(description = "언어 설정 코드", example = "ko", requiredMode = Schema.RequiredMode.REQUIRED)
    private String language_code;

    @Schema(description = "TTS 활성화 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean tts_enabled;

    @Schema(description = "조선소 구역", example = "제1조선소", requiredMode = Schema.RequiredMode.REQUIRED)
    private String shipYardArea;
}
