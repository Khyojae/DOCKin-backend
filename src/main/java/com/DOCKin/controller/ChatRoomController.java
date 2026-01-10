package com.DOCKin.controller;

import com.DOCKin.dto.chat.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name="채팅방 관리", description = "채팅방 관리")
@RestController
@RequestMapping("api/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    @Operation(summary="채팅방 생성", description = "새로운 채팅방을 생성함")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomRequestDto> createRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto){
        ChatRoomRequestDto room = new ChatRoomRequestDto();
        return ResponseEntity.ok(new ChatRoomRequestDto());
    }

    @Operation(summary="채팅방 전체 목록 조회",description = "모든 채팅방 목록을 조회함")
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> findAllRooms() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Operation(summary="채팅방 상세 조회", description="특정 채팅방 상세 조회")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> roomInfo(@PathVariable String roomId){
        return ResponseEntity.ok(null);
    }

    @Operation(summary="채팅방 수정", description ="특정 채팅방 이름 수정")
    @PutMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomUpdateRequestDto> updateRoom(
            @PathVariable String roomId,
            @RequestBody ChatRoomRequestDto updateDto){
        return ResponseEntity.ok(null);
    }

    @Operation(summary="채팅방 삭제/나가기",description = "채팅방을 삭제/나감")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId){
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="채팅 내역 조회",description = "특정 채팅방의 이전에 보냈던 메시지 조회")
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable String roomId){
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Operation(summary="메시지 보내기",description="메시지는 전역번호로")
    @PostMapping("/room/{roomId}/message")
    public ResponseEntity<ChatMessageRequestDto> createMessage(@PathVariable String roomId,
                                                               @RequestBody ChatMessageRequestDto requestDto){
        return ResponseEntity.ok(null);
    }

}
