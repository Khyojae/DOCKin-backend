package com.DOCKin.service;

import com.DOCKin.dto.chat.ChatMessageRequestDto;
import com.DOCKin.model.Chat.ChatMessages;
import com.DOCKin.model.Chat.ChatRooms;
import com.DOCKin.repository.ChatMessagesRepository;
import com.DOCKin.repository.ChatRoomsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatRoomsRepository chatRoomsRepository;

    @Async("messageExecutor")
    public void saveMessage(ChatMessageRequestDto dto){
        ChatRooms room = chatRoomsRepository.findById(dto.getRoomId())
                .orElseThrow(()->new RuntimeException("채팅방을 찾을 수 없습니다."));

        try{
            ChatMessages messages = ChatMessages.builder()
                    .chatRooms(room)
                    .senderId(dto.getSenderId())
                    .content(dto.getContent())
                    .messageType(dto.getMessageType())
                    .build();
            chatMessagesRepository.save(messages);
        } catch (Exception e){
            log.error("메시지 저장 실패: {}",e.getMessage());
        }
    }
}
