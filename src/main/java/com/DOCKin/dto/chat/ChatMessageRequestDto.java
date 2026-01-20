package com.DOCKin.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "채팅메시지 req dto")
public class ChatMessageRequestDto {

    @Schema(description = "채팅방 id",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message="채팅방 ID는 필수입니다.")
    private Integer roomId;

    @Schema(description = "보내는 사람 사원번호",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message="보내는 사람의 사원번호는 필수입니다.")
    private String senderId;

    @Schema(description = "보내는 내용")
    private String content;

    @Schema(description = "보낼 메시지 종류 (TEXT/IMAGE/FILE)",example = "File",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "메시지 종류는 필수입니다.")
    private MessageType messageType;

    @Schema(description = "파일 링크", example = "파일 보낼 때에만 사용함")
    private String fileUrl;
}

