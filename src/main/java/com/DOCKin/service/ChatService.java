package com.DOCKin.service;

import com.DOCKin.dto.chat.ChatMessageRequestDto;
import com.DOCKin.dto.chat.ChatMessageResponseDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Chat.ChatMembers;
import com.DOCKin.model.Chat.ChatMessages;
import com.DOCKin.model.Chat.ChatRooms;
import com.DOCKin.repository.Chat.ChatMembersRepository;
import com.DOCKin.repository.Chat.ChatMessagesRepository;
import com.DOCKin.repository.Chat.ChatRoomsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatMembersRepository chatMembersRepository;

    //메시지 저장
    @Async("messageExecutor")
    @Transactional
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
            room.updateLastMessage(dto.getContent(), LocalDateTime.now());

        } catch (Exception e){
            log.error("메시지 저장 실패: {}",e.getMessage());
        }
    }

    //이전 채팅 내역 불러오기
    @Transactional(readOnly = true)
    public Slice<ChatMessageResponseDto> getChatHistory(Integer roomId,String userId, Long lastMessageId,Pageable pageable){
       ChatMembers memberInfo = chatMembersRepository.findByChatRooms_RoomIdAndMember_UserId(roomId,userId)
               .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

       LocalDateTime joinedAt = memberInfo.getJoinedAt();

       Slice<ChatMessages> messages;

       if(lastMessageId==null){
           messages=chatMessagesRepository.findByChatRooms_RoomIdAndSentAtAfterOrderBySentAtDesc(roomId,joinedAt,pageable);
       }else{
           messages=chatMessagesRepository.findByChatRooms_RoomIdAndSentAtAfterAndMessageIdLessThanOrderByMessageIdDesc(roomId,joinedAt,lastMessageId,pageable);
       }
        return messages.map(ChatMessageResponseDto::from);
    }

    //메시지 검색
    @Transactional(readOnly = true)
    public Slice<ChatMessageResponseDto> searchMessage(Integer roomId, String keyword ,Pageable pageable){
        return chatMessagesRepository.findByChatRooms_RoomIdAndContentContaining(roomId,keyword,pageable)
                .map(ChatMessageResponseDto::from);
    }
}
