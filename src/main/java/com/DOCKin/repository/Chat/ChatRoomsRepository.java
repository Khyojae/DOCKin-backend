package com.DOCKin.repository.Chat;

import com.DOCKin.model.Chat.ChatRooms;
import com.DOCKin.model.Member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms,Integer> {
    @Query("SELECT r FROM ChatRooms r JOIN r.members m WHERE m.member = :member")
    Page<ChatRooms> findByMembers(Member member, Pageable pageable);
}
