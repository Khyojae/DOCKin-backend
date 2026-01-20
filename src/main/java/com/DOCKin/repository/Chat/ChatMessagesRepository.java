package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatMessages;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Long> {

    // 1. 내역 조회 및 무한 스크롤
    @Query("SELECT m FROM ChatMessages m " +
            "WHERE m.chatRooms.roomId = :roomId " +
            "AND m.sentAt > :joinedAt " +
            "AND (:lastMessageId IS NULL OR m.messageId < :lastMessageId) " +
            "ORDER BY m.messageId DESC")
    Slice<ChatMessages> findChatHistory(
            @Param("roomId") Integer roomId,
            @Param("joinedAt") LocalDateTime joinedAt,
            @Param("lastMessageId") Long lastMessageId,
            Pageable pageable);

    // 2. 키워드 검색
    @Query("SELECT m FROM ChatMessages m " +
            "WHERE m.chatRooms.roomId = :roomId " +
            "AND m.sentAt > :joinedAt " +
            "AND m.content LIKE %:keyword% " +
            "ORDER BY m.messageId DESC")
    Slice<ChatMessages> searchMessageByKeyword(
            @Param("roomId") Integer roomId,
            @Param("joinedAt") LocalDateTime joinedAt,
            @Param("keyword") String keyword,
            Pageable pageable);
}