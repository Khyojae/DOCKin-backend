package com.DOCKin.model.Chat;

import com.DOCKin.model.Member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="chat_members")
public class ChatMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRooms chatRooms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Member member;

    @Column(name="joined_at")
    private LocalDateTime joinedAt;

    @Column(name="last_read_time")
    private LocalDateTime lastReadTime;

    @PrePersist
    public void prePersist(){
        this.joinedAt=LocalDateTime.now();
        this.lastReadTime= LocalDateTime.now();
    }

    public void updateLastReadTime(LocalDateTime lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
