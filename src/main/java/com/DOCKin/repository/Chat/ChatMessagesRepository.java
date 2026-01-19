package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatMessages;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages,Long> {
    //과거 채팅 내역 조회
    Slice<ChatMessages> findByChatRooms_RoomIdAndSentAtAfterOrderBySentAtDesc(Integer roomId, LocalDateTime joinedAt, Pageable pageable);

    //채팅방 입장 시, 최신 채팅 내역 조회
    Slice<ChatMessages> findByChatRooms_RoomIdAndSentAtAfterAndMessageIdLessThanOrderByMessageIdDesc(Integer roomId, LocalDateTime joinedAt, Long lastId, Pageable pageable);

    //메시지 키워드 검색
    Slice<ChatMessages> findByChatRooms_RoomIdAndContentContaining(Integer roomId, String keyword, Pageable pageable);

    Slice<ChatMessages> findByChatRooms_RoomIdAndSentAtAfterAndContentContaining(
            Integer roomId,
            LocalDateTime joinedAt,
            String keyword,
            Pageable pageable
    );}
