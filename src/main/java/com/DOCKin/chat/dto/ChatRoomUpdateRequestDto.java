package com.DOCKin.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Schema(description = "채팅방 정보 수정 요청")
public class ChatRoomUpdateRequestDto {

    @Schema(description = "변경할 방 이름", example = "수정된 프로젝트 B팀 방")
    private String roomName;

    @Schema(description = "새롭게 추가될 참여자 ID 리스트", example = "[\"user_05\", \"user_06\"]")
    private List<String> addParticipantIds;

    @Schema(description = "제외할 참여자 ID 리스트", example = "[\"user_02\"]")
    private List<String> removeParticipantIds;

}
