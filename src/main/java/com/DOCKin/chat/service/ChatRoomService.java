package com.DOCKin.chat.service;

import com.DOCKin.chat.dto.ChatRoomRequestDto;
import com.DOCKin.chat.dto.ChatRoomResponseDto;
import com.DOCKin.chat.dto.ChatRoomUpdateRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.chat.model.ChatMembers;
import com.DOCKin.chat.model.ChatRooms;
import com.DOCKin.member.model.Member;
import com.DOCKin.chat.repository.ChatMembersRepository;
import com.DOCKin.chat.repository.ChatMessagesRepository;
import com.DOCKin.chat.repository.ChatRoomsRepository;
import com.DOCKin.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatMembersRepository chatMembersRepository;
    private final MemberRepository memberRepository;
    private final ChatMessagesRepository chatMessagesRepository;

    //채팅방 개설 (처음 채팅방 만들 때 해당)
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto, String creatorId){
        ChatRooms rooms = ChatRooms.builder()
                .roomName(dto.getRoom_name())
                .creatorId(creatorId)
                .build();

        ChatRooms savedRoom = chatRoomsRepository.save(rooms);

        //방장을 첫 번째 멤버로 저장
        saveMember(savedRoom,creatorId);

        //초대받은 참여자들 저장
        if(dto.getParticipantIds()!=null){
            dto.getParticipantIds().stream()
                    .distinct()
                    .filter(userId -> !userId.equals(creatorId))
                    .forEach(userId->saveMember(savedRoom,userId));

        }


        chatMembersRepository.flush();

        return ChatRoomResponseDto.from(savedRoom,0L);
    }

    //채팅방 목록 가져오기
    @Transactional(readOnly = true)
    public Page<ChatRoomResponseDto> getChatRooms(String userId, Pageable pageable){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        Page<ChatRooms> chatRoomsPage = chatRoomsRepository.findByMembers(member,pageable);

        return chatRoomsPage.map(room ->{
            ChatMembers participant = chatMembersRepository.findByChatRoomsRoomIdAndMemberUserId(room.getRoomId(),userId)
                    .orElseThrow(()->new BusinessException(ErrorCode.CHATMEMBER_NOT_FOUND));

            long unreadCount = chatMessagesRepository.countByChatRoomsRoomIdAndCreatedAtAfter(
                    room.getRoomId(),
                    participant.getLastReadTime()
            );
            return ChatRoomResponseDto.from(room,unreadCount);
        });
    }

    //특정 채팅방 목록 가져오기
    @Transactional
    public ChatRoomResponseDto getChatRoomsInfo(String userId,Integer roomId){

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));


        validChatRoomMember(userId,roomId);

        ChatRooms rooms = chatRoomsRepository.findById(roomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        //읽음 처리 업데이트
        ChatMembers participant = chatMembersRepository.findByChatRoomsRoomIdAndMemberUserId(roomId, userId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATMEMBER_NOT_FOUND));

        participant.updateLastReadTime(LocalDateTime.now());

        return  ChatRoomResponseDto.from(rooms,0L);
    }

    //유저가 채팅방 멤버인지 입증
    @Transactional(readOnly = true)
    public void validChatRoomMember(String userId, Integer roomId){

        boolean isMember = chatMembersRepository.existsByChatRoomsRoomIdAndMemberUserId(roomId, userId);
        if(!isMember){
            throw new BusinessException(ErrorCode.CHATROOM_AUTHOR);
        }

    }


    //채팅방 수정
    @Transactional
    public ChatRoomResponseDto reviseChatRoom(Integer chatRoomId,String creatorId,ChatRoomUpdateRequestDto dto){
        ChatRooms rooms = chatRoomsRepository.findById(chatRoomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        // 권한 확인
        if(!rooms.getCreatorId().equals(creatorId)){
            throw new BusinessException(ErrorCode.CHATROOM_AUTHOR);
        }

        //방 이름 수정
        if(dto.getRoomName()!=null){
            rooms.updateRoomName(dto.getRoomName());
        }

        //멤버 삭제(방장 제외)
        if(dto.getRemoveParticipantIds()!=null){
            dto.getRemoveParticipantIds().forEach(removeId->{
                if(!removeId.equals(rooms.getCreatorId())){
                    chatMembersRepository.deleteByChatRoomsAndMemberUserId(rooms,removeId);
                    rooms.removeMember(removeId);
                }
                rooms.getMembers().removeIf(m -> m.getMember().getUserId().equals(removeId));
            });
            chatMembersRepository.flush();
        }

        //새로운 멤버 추가
        if(dto.getAddParticipantIds()!=null){
            dto.getAddParticipantIds().forEach(addId->{
                boolean isAlreadyMember = chatMembersRepository.existsByChatRoomsAndMemberUserId(rooms,addId);
                if(!isAlreadyMember){
                    saveMember(rooms,addId);
                }
            });
            chatMembersRepository.flush();
        }

        long finalMemberCount = chatMembersRepository.countByChatRooms(rooms);
        if(finalMemberCount<2){
            throw new IllegalArgumentException("채팅방에서는 최소 2명의 참가자가 있어야 합니다.");
        }
        chatMembersRepository.flush();
        ChatRooms updatedRoom = chatRoomsRepository.findById(chatRoomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        return ChatRoomResponseDto.from(updatedRoom,0L);
    }

    //채팅방 삭제 (말그대로 채팅방 삭제)
    @Transactional
    public void deleteChatRoom(Integer chatRoomId, String userId){
        ChatRooms chatRooms = chatRoomsRepository.findById(chatRoomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        if(!chatRooms.getCreatorId().equals(userId)){
            throw new BusinessException(ErrorCode.CHATROOM_AUTHOR);
        }
        chatRoomsRepository.delete(chatRooms);
    }

    //채팅방 나가기 (한명만)
    @Transactional
    public void leaveChatRoom(Integer chatRoomId, String userId){
        ChatRooms room = chatRoomsRepository.findById(chatRoomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));
        chatMembersRepository.deleteByChatRoomsAndMemberUserId(room,userId);
        long memberCount = chatMembersRepository.countByChatRooms(room);
        if(memberCount==0){
            chatRoomsRepository.delete(room);
        }
    }

    //멤버 초대
    private void saveMember(ChatRooms rooms, String userId){
        Member memberEntity = memberRepository.findById(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChatMembers chatMember = ChatMembers.builder()
                .chatRooms(rooms)
                .member(memberEntity)
                .joinedAt(LocalDateTime.now())
                .build();
        chatMembersRepository.save(chatMember);

        if (rooms.getMembers() != null) {
            rooms.getMembers().add(chatMember);
        }
    }

    //각 멤버에게 라우팅
    @Transactional(readOnly = true)
    public List<String> getParticipantsIds(Integer roomId){
        return chatMembersRepository.findByChatRoomsRoomId(roomId)
                .stream()
                .map(chatMember->chatMember.getMember().getUserId())
                .collect(Collectors.toList());
    }

}
