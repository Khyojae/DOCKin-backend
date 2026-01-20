package com.DOCKin.model.Chat;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    @Column(name="creator_id")
    private String creatorId;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_message_content")
    private String lastMessageContent;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Builder.Default
    @OneToMany(mappedBy = "chatRooms", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<ChatMembers> members = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
        if(this.isGroup==null) this.isGroup=false;
    }

    public void updateLastMessage(String content, LocalDateTime sentAt){
        this.lastMessageContent = content;
        this.lastMessageAt=sentAt;
    }

    public void updateRoomName(String room_name){
        if(room_name==null || room_name.isBlank()){
            throw new IllegalArgumentException("방 이름은 필수입니다.");
        }
        this.roomName=room_name;
    }

    public void removeMember(String userId){
        this.members.removeIf(m->m.getMember().getUserId().equals(userId));
    }


}
