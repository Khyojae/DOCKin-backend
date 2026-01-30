package com.DOCKin.chat.model;

import com.DOCKin.chat.dto.MessageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Table(name = "chat_messages",
indexes = {@Index(name="idx_room_sent",columnList = "room_id, sent_at")})
public class ChatMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRooms chatRooms;

    @Column(name = "sender_id")
    @NotNull
    private String senderId;

    @Column(name = "content", columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    @NotNull
    private MessageType messageType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
        if (this.messageType == null) {
            this.messageType = MessageType.TEXT; // 기본값 설정
        }

}
}
