package com.DOCKin.repository;

import com.DOCKin.model.Chat.ChatMembers;
import com.DOCKin.model.Chat.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ChatMembersRepository extends JpaRepository<ChatMembers,Integer> {
boolean existsByChatRoomsAndMember_UserId(ChatRooms chatRooms, String userId);

@Modifying
    @Transactional
    void deleteByChatRoomsAndMember_UserId(ChatRooms chatRooms,String userId);

Optional<ChatMembers> findByChatRoomsAndMember_UserId(ChatRooms chatRooms,String userId);
}
