package com.DOCKin.controller;

import com.DOCKin.dto.chat.*;
import com.DOCKin.global.security.auth.CustomUserDetails;
import com.DOCKin.service.ChatRoomService;
import com.DOCKin.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
            @PageableDefault(size = 20,direction = Sort.Direction.DESC)Pageable pageable
            ) {
        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(chatRoomService.getChatRooms(userId,pageable));
    }

    @Operation(summary="채팅방 상세 조회", description="특정 채팅방 상세 조회")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> roomInfo(@PathVariable Integer roomId,
                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(chatRoomService.getChatRoomsInfo(userId,roomId));
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

    @Operation(summary="채팅방 나가기",description = "채팅방을 나간다")
    @DeleteMapping("/room/leave/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Integer roomId,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String userId = customUserDetails.getMember().getUserId();
        chatRoomService.leaveChatRoom(roomId,userId);

        return ResponseEntity.noContent().build();
    }

   @Operation(summary="채팅 내역 조회",description = "특정 채팅방의 메시지를 조회 (입장 시점 이후 메시지만 조회 가능), 무한 스크롤 가능")
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<Slice<ChatMessageResponseDto>> getChatMessages(@PathVariable Integer roomId,
                                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                         @RequestParam(required = false) Long lastMessageId,
                                                                         @PageableDefault(size = 20, sort = "messageId", direction = Sort.Direction.DESC) Pageable pageable){
       String userId = customUserDetails.getMember().getUserId();
        Slice<ChatMessageResponseDto> chatHistory= chatService.getChatHistory(roomId,userId,lastMessageId,pageable);
        return ResponseEntity.ok(chatHistory);
    }

    @Operation(summary ="채팅방에서 특정 키워드 조회")
    @GetMapping("/room/{roomId}/messages/search")
    public ResponseEntity<Slice<ChatMessageResponseDto>> searchMessages(@PathVariable Integer roomId,
                                                                        @RequestParam String keyword,
                                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                        @PageableDefault(sort = "messageId", direction = Sort.Direction.DESC) Pageable pageable){

        String userId = customUserDetails.getMember().getUserId();
        return ResponseEntity.ok(chatService.searchMessage(roomId,userId,keyword,pageable));
    }
}
