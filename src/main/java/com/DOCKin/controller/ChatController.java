package com.DOCKin.controller;

import com.DOCKin.dto.chat.ChatMessageRequestDto;
import com.DOCKin.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto message, SimpMessageHeaderAccessor headerAccessor){

        //세션에서 인증된 실제 userId 추출
        if (headerAccessor.getSessionAttributes() != null) {
            String actualUserId = (String) headerAccessor.getSessionAttributes().get("userId");
            if (actualUserId != null) {
                message.setSenderId(actualUserId);
            }
        }


        log.info("메시지 수신: 방번호={}, 보낸이={}, 내용={}",
                message.getRoomId(),message.getSenderId(),message.getContent());

        String destination = "/sub/chat/room/" + String.valueOf(message.getRoomId());
        log.info("==> [발송 경로 확인]: {}", destination);

        messagingTemplate.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
        messagingTemplate.convertAndSend("/sub/chat/rooms/update", message);
        chatService.saveMessage(message);
    }


}
