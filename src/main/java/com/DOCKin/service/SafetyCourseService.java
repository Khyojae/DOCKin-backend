package com.DOCKin.service;

import com.DOCKin.dto.SafetyCourse.SafetyCourseCreateRequestDto;
import com.DOCKin.dto.SafetyCourse.SafetyCourseResponseDto;
import com.DOCKin.dto.SafetyCourse.SafetyCourseUpdateRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Member.Member;
import com.DOCKin.model.SafetyCourse.SafetyCourse;
import com.DOCKin.repository.Member.MemberRepository;
import com.DOCKin.repository.SafetyCourse.SafetyCourseRepository;
import com.DOCKin.repository.SafetyCourse.SafetyEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafetyCourseService {
    private final SafetyCourseRepository safetyCourseRepository;
    private final SafetyEnrollmentRepository safetyEnrollmentRepository;
    private final MemberRepository memberRepository;

    //교육 자료 등록
    @Transactional
    public SafetyCourseResponseDto safetyCourseResponse(SafetyCourseCreateRequestDto dto, String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        SafetyCourse safetyCourse = SafetyCourse.builder().
                title(dto.getTitle()).
                description(dto.getDescription()).
                materialUrl(dto.getVideoUrl()).
                durationMinutes(dto.getDurationMinutes()).
                createdBy(member.getUserId()).build();

        SafetyCourse savedCourse = safetyCourseRepository.save(safetyCourse);

        return SafetyCourseResponseDto.fromEntity(savedCourse);
    }

    //교육 자료 수정
    @Transactional
    public SafetyCourseResponseDto safetyCourseResponseDto(SafetyCourseUpdateRequestDto dto, String userId){

       SafetyCourse logs = safetyCourseRepository.findById(dto.getCourseId())
               .orElseThrow(()->new BusinessException(ErrorCode.SAFETYCOURSE_NOT_FOUND));

               //같은 수정자 인지 확인
       if(!dto.getUserId().equals(userId)){
           throw new BusinessException(ErrorCode.SAFETYCOURSE_AUTHOR);
       }

       if(dto.getCourseId()!=null){
           logs.setCourseId(dto.getCourseId());
       }
        if(dto.getTitle()!=null){
            logs.setTitle(dto.getTitle());
        }
        if(dto.getDescription()!=null){
            logs.setDescription(dto.getDescription());
        }
        if(dto.getVideoUrl()!=null){
            logs.setVideoUrl(dto.getVideoUrl());
        }
        if(dto.getDurationMinutes()!=null){
            logs.setDurationMinutes(dto.getDurationMinutes());
        }
        return SafetyCourseResponseDto.fromEntity(safetyCourseRepository.save(logs));
    }

    //교육자료 조회
    @Transactional(readOnly = true)
    public Page<SafetyCourseResponseDto> readSafetyCourse(String userId, Pageable pageable){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        Page<SafetyCourse> safetyCourses = safetyCourseRepository.findAll(pageable);
        return safetyCourses.map(SafetyCourseResponseDto::fromEntity);
    }

    //교육자료 삭제
    @Transactional(readOnly = true)
    public void  deleteSafetyCourse(String userId, Integer courseId){
        SafetyCourse safetyCourse =safetyCourseRepository.findById(courseId)
                .orElseThrow(()->new BusinessException(ErrorCode.SAFETYCOURSE_NOT_FOUND));

        if(!safetyCourse.getCourseId().equals(courseId)){
            throw new BusinessException(ErrorCode.SAFETYCOURSE_AUTHOR);
        }

        safetyCourseRepository.delete(safetyCourse);
    }

}
