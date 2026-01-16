package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatMessages;
import com.DOCKin.model.Chat.ChatRooms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages,Long> {
    Slice<ChatMessages> findByChatRoomsOrderBySentAtDesc(ChatRooms chatRooms, Pageable pageable);
    Slice<ChatMessages> findByChatRoomsRoomIdOrderBySentAtDesc(String roomId, Pageable pageable);
    Slice<ChatMessages> findByRoomIdOrderByCreatedAtDesc(String roomId,Pageable pageable);
}
