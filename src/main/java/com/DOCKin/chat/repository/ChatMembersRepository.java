package com.DOCKin.chat.repository;

import com.DOCKin.chat.model.ChatMembers;
import com.DOCKin.chat.model.ChatRooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface ChatMembersRepository extends JpaRepository<ChatMembers,Integer> {
boolean existsByChatRoomsAndMemberUserId(ChatRooms chatRooms, String userId);

@Modifying
    @Transactional
    void deleteByChatRoomsAndMemberUserId(ChatRooms chatRooms,String userId);

    long countByChatRooms(ChatRooms rooms);

    Optional<ChatMembers> findByChatRoomsRoomIdAndMemberUserId(Integer roomId, String userId);

    boolean existsByChatRoomsRoomIdAndMemberUserId(Integer roomId, String userId);

    List<ChatMembers> findByChatRoomsRoomId(Integer roomId);

    @Modifying @Transactional
    @Query(value = "UPDATE chat_members SET last_read_time = NOW() WHERE room_id = :roomId AND user_id = :userId", nativeQuery = true)
    void updateLastReadTimeNative(@Param("roomId") Integer roomId, @Param("userId") String userId);

}
