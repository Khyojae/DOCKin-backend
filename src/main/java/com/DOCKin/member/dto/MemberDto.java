package com.DOCKin.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "멤버 정보 조회 res dto")
public class MemberDto {
    @Schema(description = "사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
