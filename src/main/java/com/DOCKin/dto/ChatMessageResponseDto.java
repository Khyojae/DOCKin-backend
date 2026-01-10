package com.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDto {
    private Integer roomId;
    private String senderId;
    private String messageId;
    private String content;
    private String fileUrl;
    private MessageType messageType;
    private LocalDateTime sentAt;

}
