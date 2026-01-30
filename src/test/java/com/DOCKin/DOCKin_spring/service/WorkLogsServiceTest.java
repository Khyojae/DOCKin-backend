package com.DOCKin.DOCKin_spring.service;

import com.DOCKin.worklog.dto.WorkLogsCreateRequestDto;
import com.DOCKin.worklog.dto.WorkLogsUpdateRequestDto;
import com.DOCKin.worklog.dto.Work_logsDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.worklog.model.Equipment;
import com.DOCKin.member.model.Member;
import com.DOCKin.worklog.model.Work_logs;
import com.DOCKin.worklog.repository.EquipmentRepository;
import com.DOCKin.member.repository.MemberRepository;
import com.DOCKin.worklog.repository.Work_logsRepository;
import com.DOCKin.worklog.service.WorkLogsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkLogsServiceTest {

    @Mock
    private Work_logsRepository workLogsRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private WorkLogsService workLogsService;

    @Test
    @DisplayName("작업일지 수정 성공 - 작성자가 본인일 때")
    void updateWorklog_Success() {
        // given (준비)
        Equipment equipment = Equipment.builder()
                .equipmentId(1L)
                .build();

        String userId = "user123";
        Long logId = 1L;

        Member member = Member.builder().userId(userId).build();
        Work_logs existingLog = Work_logs.builder()
                .logId(logId)
                .title("원래 제목")
                .member(member)
                .equipment(equipment)
                .build();

        WorkLogsUpdateRequestDto requestDto = new WorkLogsUpdateRequestDto();
        requestDto.setTitle("수정된 제목");

        given(workLogsRepository.findById(logId)).willReturn(Optional.of(existingLog));

        // when (실행)
        Work_logsDto result = workLogsService.updateWorklog(userId, logId, requestDto);

        // then (검증)
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(existingLog.getTitle()).isEqualTo("수정된 제목"); // Dirty Checking 확인
    }

    @Test
    @DisplayName("작업일지 수정 실패 - 작성자가 아닐 때 예외 발생")
    void updateWorklog_Fail_NotAuthor() {
        // given
        String userId = "user123";
        String otherUserId = "other456";
        Long logId = 1L;

        Member author = Member.builder().userId(otherUserId).build();
        Work_logs existingLog = Work_logs.builder().member(author).build();

        WorkLogsUpdateRequestDto requestDto = new WorkLogsUpdateRequestDto();

        given(workLogsRepository.findById(logId)).willReturn(Optional.of(existingLog));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            workLogsService.updateWorklog(userId, logId, requestDto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_LOG_AUTHOR);
    }


    @Test
    @DisplayName("게시물 작성 성공")
    void createWorklog_Success() {
        // given
        String userId = "user1";
        WorkLogsCreateRequestDto dto = new WorkLogsCreateRequestDto("새 일지", "내용", "url", 1L);

        // 가짜 멤버와 장비 준비
        Member member = Member.builder().userId(userId).build();
        Equipment equipment = Equipment.builder().equipmentId(1L).build();

        // 가짜 저장 결과 (ID가 할당된 객체)
        Work_logs savedLog = Work_logs.builder()
                .logId(50L)
                .title(dto.getTitle())
                .member(member)
                .equipment(equipment)
                .build();

        // 레포지토리 동작 정의
        given(memberRepository.findByUserId(userId)).willReturn(Optional.of(member));
        given(equipmentRepository.findById(1L)).willReturn(Optional.of(equipment));
        given(workLogsRepository.save(any(Work_logs.class))).willReturn(savedLog);

        // when
        Work_logsDto result = workLogsService.createWorklog(userId, dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("새 일지");
        verify(workLogsRepository, times(1)).save(any(Work_logs.class));
    }

    @Test
    @DisplayName("같은 구역 작업자의 일지 조회 성공")
    void readOtherWorklog_Success() {
        // given
        String myId = "me";
        String targetId = "other";
        Pageable pageable = PageRequest.of(0, 10);

        // 둘 다 "Area-A" 구역 소속
        Member me = Member.builder().userId(myId).shipYardArea("Area-A").build();
        Member other = Member.builder().userId(targetId).shipYardArea("Area-A").build();

        // 조회될 가짜 데이터
        Equipment equipment = Equipment.builder().equipmentId(1L).build(); // NPE 방지용
        Work_logs otherLog = Work_logs.builder().title("상대방 글").member(other).equipment(equipment).build();
        Page<Work_logs> page = new PageImpl<>(List.of(otherLog));

        given(memberRepository.findByUserId(myId)).willReturn(Optional.of(me));
        given(memberRepository.findByUserId(targetId)).willReturn(Optional.of(other));
        given(workLogsRepository.findAllByMemberUserId(targetId, pageable)).willReturn(page);

        // when
        Page<Work_logsDto> result = workLogsService.readOtherWorklog(myId, targetId, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1); // 전체 요소 개수 확인
        assertThat(result.getNumberOfElements()).isEqualTo(1); // 현재 페이지의 요소 개수 확인
    }

    @Test
    @DisplayName("다른 구역 작업자의 일지 조회 시 ACCESS_DENIED 예외 발생")
    void readOtherWorklog_Fail_DifferentArea() {
        // given
        String myId = "me";
        String targetId = "other";

        // 구역이 서로 다름
        Member me = Member.builder().userId(myId).shipYardArea("Area-A").build();
        Member other = Member.builder().userId(targetId).shipYardArea("Area-B").build();

        given(memberRepository.findByUserId(myId)).willReturn(Optional.of(me));
        given(memberRepository.findByUserId(targetId)).willReturn(Optional.of(other));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            workLogsService.readOtherWorklog(myId, targetId, PageRequest.of(0, 10));
        });

        // 내가 정의한 에러 코드가 맞는지 확인
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
    }
}
