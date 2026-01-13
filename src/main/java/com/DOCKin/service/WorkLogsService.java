package com.DOCKin.service;

import com.DOCKin.dto.WorkLogs.WorkLogsCreateRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


        Work_logs savedlog = workLogsRepository.save(work_logs);
            return Work_logsDto.builder()
                    .log_id(savedlog.getLog_id())
                    .user_id(member.getUserId())
                    .equipment_id(savedlog.getEquipment().getEquipment_id())
                    .title(savedlog.getTitle())
                    .log_text(savedlog.getLog_text())
                    .image_url(savedlog.getImage_url())
                    .created_at(savedlog.getCreated_at())
                    .updated_at(savedlog.getUpdated_at())
                    .build();
    }
}
