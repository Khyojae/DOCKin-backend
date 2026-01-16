package com.DOCKin.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅방 req dto")
public class ChatRoomRequestDto {
    @Schema(description = "채팅방 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "채팅방 이름은 필수입니다.")
    private String room_name;

    @Schema(description = "그룹채팅방인지 그냥 1대1채팅방인지", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Boolean is_group;

    @Schema(description = "방장 id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String creatorId;

    @Schema(description = "참가하는 인원의 사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "최소 한 명 이상의 참가자가 필요합니다.")
    private List<String> participantIds;
}
