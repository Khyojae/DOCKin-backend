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

    //@Async
    @Transactional
    public void saveMessage(ChatMessageRequestDto dto) {
        log.info("### [시작] 동기 방식으로 실행");

        // 1. 메시지 저장만 JPA 사용
        ChatRooms room = chatRoomsRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("방 없음"));

        ChatMessages msg = ChatMessages.builder()
                .chatRooms(room)
                .senderId(dto.getSenderId())
                .content(dto.getContent())
                .messageType(dto.getMessageType())
                .build();
        chatMessagesRepository.saveAndFlush(msg);
        log.info("### [1] 메시지 저장 완료");

        // 2. 나머지는 무조건 Native Query로 실행 (JPA Dirty Checking 회피)
        // 여기서 핵심은 room 객체의 필드를 절대 setter로 고치지 않는 것입니다!
        chatMembersRepository.updateLastReadTimeNative(dto.getRoomId(), dto.getSenderId());
        log.info("### [2] 멤버 업데이트(Native) 완료");

        chatRoomsRepository.updateLastMessageNative(dto.getRoomId(), dto.getContent());
        log.info("### [3] 방 업데이트(Native) 완료");
    }

    //이전 채팅 내역 불러오기
    @Transactional(readOnly = true)
    public Slice<ChatMessageResponseDto> getChatHistory(Integer roomId,String userId, Long lastMessageId,Pageable pageable){
       ChatMembers memberInfo = chatMembersRepository.findByChatRooms_RoomIdAndMember_UserId(roomId,userId)
               .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

       Slice<ChatMessages> messages = chatMessagesRepository.findChatHistory(
               roomId,
               memberInfo.getJoinedAt(),
               lastMessageId,
               pageable
       );
        return messages.map(ChatMessageResponseDto::from);
    }

    //메시지 검색
    @Transactional(readOnly = true)
    public Slice<ChatMessageResponseDto> searchMessage(Integer roomId, String userId ,String keyword ,Pageable pageable){
        ChatMembers members = chatMembersRepository.findByChatRooms_RoomIdAndMember_UserId(roomId,userId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATMEMBER_NOT_FOUND));

        Slice<ChatMessages> messages = chatMessagesRepository
                .searchMessageByKeyword(
                        roomId,
                        members.getJoinedAt(),
                        keyword,
                        pageable
                );
        return messages.map(ChatMessageResponseDto::from);
    }
}
