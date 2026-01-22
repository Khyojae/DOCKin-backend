package com.DOCKin.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @JsonProperty("room_name")
    @NotBlank(message = "채팅방 이름은 필수입니다.")
    private String room_name;

    @Schema(description = "참가하는 인원의 사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min=1, message="채팅방을 생성하려면 본인을 포함해 최소 1명의 참가자가 필요합니다.")
    private List<String> participantIds;



}
