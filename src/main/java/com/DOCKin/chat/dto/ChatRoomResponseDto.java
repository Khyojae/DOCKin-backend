package com.DOCKin.chat.dto;

import com.DOCKin.chat.model.ChatRooms;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅방 res dto")
public class ChatRoomResponseDto {
    @Schema(description = "채팅방 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomName;

    @Schema(description = "채팅방 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer roomId;

    @Schema(description = "참가하는 인원의 사원번호", minLength = 2, requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> participantIds;

    @Schema(description = "방장id",requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorId;

    @Schema(description = "만든 시각", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 메시지 내용",requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastMessageContent;

    @Schema(description = "마지막 메시지 시간",requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime lastMessageAt;

    @Schema(description = "안 읽은 메시지 수",requiredMode = Schema.RequiredMode.REQUIRED)
    private long unreadCount;

    public static ChatRoomResponseDto from(ChatRooms entity, long unreadCount){
        return ChatRoomResponseDto.builder()
                .roomId(entity.getRoomId())
                .roomName(entity.getRoomName())
                .creatorId(entity.getCreatorId())
                .createdAt(entity.getCreatedAt())
                .participantIds(entity.getMembers().stream().map(member->member.getMember().getUserId()).collect(Collectors.toList()))
                .lastMessageContent(entity.getLastMessageContent())
                .lastMessageAt(entity.getLastMessageAt())
                .unreadCount(unreadCount)
                .build();
    }
}
