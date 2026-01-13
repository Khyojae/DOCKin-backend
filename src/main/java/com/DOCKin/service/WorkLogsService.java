package com.DOCKin.service;

import com.DOCKin.dto.WorkLogs.WorkLogsCreateRequestDto;
import com.DOCKin.dto.WorkLogs.WorkLogsUpdateRequestDto;
import com.DOCKin.dto.WorkLogs.Work_logsDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Equipment;
import com.DOCKin.model.Member.Member;
import com.DOCKin.model.Work_logs;
import com.DOCKin.repository.EquipmentRepository;
import com.DOCKin.repository.MemberRepository;
import com.DOCKin.repository.Work_logsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkLogsService {

    private final Work_logsRepository workLogsRepository;
    private final MemberRepository memberRepository;
    private final EquipmentRepository equipmentRepository;

    //게시물 작성
    @Transactional
    public Work_logsDto createWorklog(String userId,WorkLogsCreateRequestDto dto){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(()->new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));

        Work_logs work_logs = Work_logs.builder()
                .title(dto.getTitle())
                .log_text(dto.getLog_text())
                .image_url(dto.getImage_url())
                .equipment(equipment)
                .member(member)
                .build();

         return Work_logsDto.from(workLogsRepository.save(work_logs));
    }

    //게시물 조회
    @Transactional(readOnly = true)
    public Page<Work_logsDto> readWorklog(String userId, Pageable pageable){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
        String area = member.getShipYardArea();
       List<Member> areaMembers= memberRepository.findByShipYardArea(area);
       Page<Work_logs> logs = workLogsRepository.findByMemberIn(areaMembers,pageable);

       return logs.map(Work_logsDto::from);
    }

    //게시물 수정
    @Transactional
    public Work_logsDto updateWorklog(String userId, Long logId, WorkLogsCreateRequestDto dto){
        Work_logs logs = workLogsRepository.findById(logId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        //작성자와 수정자가 같은지 확인
        if(!logs.getMember().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if(dto.getTitle()!=null) logs.setTitle(dto.getTitle());
        if(dto.getLog_text()!=null) logs.setLog_text(dto.getLog_text());
        if(dto.getImage_url()!=null) logs.setImage_url(dto.getImage_url());
        if(dto.getEquipmentId()!=null){
            Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                    .orElseThrow(()->new BusinessException(ErrorCode.EQUIPMENT_NOT_FOUND));
            logs.setEquipment(equipment);
        }

        return Work_logsDto.from(logs);
    }
}
