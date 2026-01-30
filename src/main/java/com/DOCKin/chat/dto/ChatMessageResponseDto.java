package com.DOCKin.chat.dto;

import com.DOCKin.chat.model.ChatMessages;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(description = "채팅메시지 res dto")
public class ChatMessageResponseDto {
    @Schema(description = "채팅방 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer roomId;

    @Schema(description = "보내는 사람 사원번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senderId;

    @Schema(description = "메시지 번호", example = "이건 전역번호로 설정함, 채팅방마다 번호 매기지 x", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long messageId;

    @Schema(description = "보내는 내용",requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "보내는 첨부파일의 주소",example = "파일 보낼 때만 사용함")
    private String fileUrl;

    @Schema(description = "보낼 메시지 종류 (TEXT/IMAGE/FILE)",example = "File",requiredMode = Schema.RequiredMode.REQUIRED)
    private MessageType messageType;

    @Schema(description = "보낸 시각", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime sentAt;


    public static ChatMessageResponseDto from(ChatMessages entity) {
        return ChatMessageResponseDto.builder()
                .roomId(entity.getChatRooms().getRoomId())
                .senderId(entity.getSenderId())
                .messageId(entity.getMessageId())
                .content(entity.getContent())
                .fileUrl(entity.getFileUrl())
                .messageType(entity.getMessageType())
                .sentAt(entity.getSentAt())
                .build();
    }
}
