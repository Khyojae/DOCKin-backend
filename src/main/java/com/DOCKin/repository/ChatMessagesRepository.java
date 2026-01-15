package com.DOCKin.repository;

import com.DOCKin.model.Chat.ChatMessages;
import com.DOCKin.model.Chat.ChatRooms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages,Long> {
    Slice<ChatMessages> findByChatRoomsOrderBySentAtDesc(ChatRooms chatRooms, Pageable pageable);
    Slice<ChatMessages> findByChatRoomsRoomIdOrderBySentAtDesc(Long roomId, Pageable pageable);
}
