package com.DOCKin.controller;

import com.DOCKin.dto.chat.*;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.model.Chat.ChatRooms;
import com.DOCKin.service.ChatRoomService;
import com.DOCKin.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name="채팅방 관리", description = "채팅방 관리")
@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Operation(summary="채팅방 생성", description = "새로운 채팅방을 생성함")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponseDto> createRoom(@Valid @RequestBody ChatRoomRequestDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String creatorId = customUserDetails.getMember().getUserId();
        ChatRoomResponseDto chatRooms = chatRoomService.createChatRoom(dto,creatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRooms);
    }

    @Operation(summary="채팅방 전체 목록 조회",description = "모든 채팅방 목록을 조회함")
    @GetMapping("/rooms")
    public ResponseEntity<Page<ChatRoomResponseDto>> findAllRooms(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PageableDefault(size = 10,direction = Sort.Direction.DESC)Pageable pageable
            ) {
        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(chatRoomService.getChatRooms(userId,pageable));
    }

    @Operation(summary="채팅방 상세 조회", description="특정 채팅방 상세 조회")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> roomInfo(@PathVariable Integer roomId){
        return ResponseEntity.ok(chatRoomService.getChatRoomsInfo(roomId));
    }


    @Operation(summary="채팅방 수정", description ="특정 채팅방 이름 수정")
    @PutMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> updateRoom(
            @PathVariable Integer roomId,
           @Valid @RequestBody ChatRoomUpdateRequestDto updateDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String creatorId = customUserDetails.getMember().getUserId();
        ChatRoomResponseDto dto =chatRoomService.reviseChatRoom(roomId,creatorId,updateDto);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary="채팅방 삭제",description = "채팅방을 삭제")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer roomId,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String creatorId = customUserDetails.getMember().getUserId();
        chatRoomService.deleteChatRoom(roomId,creatorId);
        return ResponseEntity.noContent().build();
    }

   /* @Operation(summary="채팅 내역 조회",description = "특정 채팅방의 이전에 보냈던 메시지 조회")
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<Slice<ChatMessageResponseDto>> getChatMessages(@PathVariable String roomId){
        return ResponseEntity.ok(Collections.emptyList());
    } */

}
