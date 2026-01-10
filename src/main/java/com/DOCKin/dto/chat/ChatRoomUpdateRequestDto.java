package com.DOCKin.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅방 정보 수정 요청")
public class ChatRoomUpdateRequestDto {

    @Schema(description = "변경할 방 이름", example = "수정된 프로젝트 B팀 방")
    private String room_name;

    @Schema(description = "그룹 채팅 여부 변경 (필요 시)", example = "true")
    private Boolean is_group;

    @Schema(description = "새롭게 추가될 참여자 ID 리스트", example = "['user_05', 'user_06']")
    private List<String> addParticipantIds;

    @Schema(description = "제외할 참여자 ID 리스트", example = "['user_02']")
    private List<String> removeParticipantIds;
}
