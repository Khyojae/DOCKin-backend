package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatMessages;
import com.DOCKin.model.Chat.ChatRooms;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages,Long> {

    Slice<ChatMessages> findByChatRooms_RoomIdOrderBySentAtDesc(Integer roomId, Pageable pageable);
}
