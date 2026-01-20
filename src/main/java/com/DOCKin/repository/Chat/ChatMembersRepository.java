package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatMembers;
import com.DOCKin.model.Chat.ChatRooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface ChatMembersRepository extends JpaRepository<ChatMembers,Integer> {
boolean existsByChatRoomsAndMember_UserId(ChatRooms chatRooms, String userId);

@Modifying
    @Transactional
    void deleteByChatRoomsAndMember_UserId(ChatRooms chatRooms,String userId);

    long countByChatRooms(ChatRooms rooms);

    Optional<ChatMembers> findByChatRooms_RoomIdAndMember_UserId(Integer roomId, String userId);

    boolean existsByChatRooms_RoomIdAndMember_UserId(Integer roomId, String userId);

    List<ChatMembers> findByChatRooms_RoomId(Integer roomId);

    @Modifying @Transactional
    @Query(value = "UPDATE chat_members SET last_read_time = NOW() WHERE room_id = :roomId AND user_id = :userId", nativeQuery = true)
    void updateLastReadTimeNative(@Param("roomId") Integer roomId, @Param("userId") String userId);

}
