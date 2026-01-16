package com.DOCKin.model.Chat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="chat_rooms")
public class ChatRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Integer roomId;

    @Column(name="room_name")
    private String roomName;

    @Column(name="is_group")
    private Boolean isGroup;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_message_content")
    private String lastMessageContent;

    @Column(name = "last_messsage_at")
    private LocalDateTime lastMessageAt;

    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
        if(this.isGroup==null) this.isGroup=false;
    }

    public void updateLastMessage(String content, LocalDateTime sentAt){
        this.this.lastMessageContent = content;
        this.lastMessageAt=sentAt;
    }

}
