package com.DOCKin.service;

import com.DOCKin.dto.chat.ChatRoomRequestDto;
import com.DOCKin.dto.chat.ChatRoomResponseDto;
import com.DOCKin.dto.chat.ChatRoomUpdateRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Chat.ChatMembers;
import com.DOCKin.model.Chat.ChatRooms;
import com.DOCKin.model.Member.Member;
import com.DOCKin.repository.ChatMembersRepository;
import com.DOCKin.repository.ChatRoomsRepository;
import com.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatMembersRepository chatMembersRepository;
    private final MemberRepository memberRepository;

    //채팅방 개설 (처음 채팅방 만들 때 해당)
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto, String creatorId){
        ChatRooms rooms = ChatRooms.builder()
                .roomName(dto.getRoom_name())
                .isGroup(dto.getIs_group())
                .creatorId(dto.getCreatorId())
                .build();

        ChatRooms savedRoom = chatRoomsRepository.save(rooms);

        //방장을 첫 번째 멤버로 저장
        saveMember(savedRoom,creatorId);

        //초대받은 참여자들 저장
        if(dto.getParticipantIds()!=null){
            dto.getParticipantIds().stream()
                    .filter(userId -> !userId.equals(creatorId))
                    .forEach(userId->saveMember(savedRoom,userId));

        }
        return ChatRoomResponseDto.from(savedRoom);
    }

    //채팅방 목록 가져오기
    @Transactional(readOnly = true)
    public Page<ChatRoomResponseDto> getChatRoom(String userId, Pageable pageable){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        String area = chatRoomsRepository.fin

    }


    //채팅방 수정
    @Transactional
    public ChatRoomResponseDto reviseChatRoom(ChatRoomUpdateRequestDto dto,String creatorId,Integer chatRoomId){
        ChatRooms rooms = chatRoomsRepository.findById(chatRoomId)
                .orElseThrow(()->new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        // 권한 확인
        if(!rooms.getCreatorId().equals(creatorId)){
            throw new BusinessException(ErrorCode.CHATROOM_AUTHOR);
        }

        //방 이름 수정
        if(dto.getRoom_name()!=null){
            rooms.updateRoomName(dto.getRoom_name());
        }

        //새로운 멤버 추가
        if(dto.getAddParticipantIds()!=null){
            dto.getAddParticipantIds().forEach(addId->{
                boolean isAlreadyMember = chatMembersRepository.existsByChatRoomsAndMember_UserId(rooms,addId);
                if(!isAlreadyMember){
                    saveMember(rooms,addId);
                }
            });
        }

        //멤버 삭제
        if(dto.getRemoveParticipantIds()!=null){
            dto.getRemoveParticipantIds().forEach(removeId->{
                if(!removeId.equals(rooms.getCreatorId())){
                    chatMembersRepository.deleteByChatRoomsAndMember_UserId(rooms,removeId);
                }
            });
        }
        return ChatRoomResponseDto.from(rooms);
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
    }


}
