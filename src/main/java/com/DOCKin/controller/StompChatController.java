package com.DOCKin.controller;

import com.DOCKin.dto.chat.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto message){

        // 로직
        messagingTemplate.convertAndSend("/sub/room"+message.getRoomId(),message);
    }
}
