package com.DOCKin.safetyCourse.service;

import com.DOCKin.safetyCourse.dto.SafetyCourseCreateRequestDto;
import com.DOCKin.safetyCourse.dto.SafetyCourseResponseDto;
import com.DOCKin.safetyCourse.dto.SafetyCourseUpdateRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.member.model.Member;
import com.DOCKin.member.model.UserRole;
import com.DOCKin.safetyCourse.model.SafetyCourse;
import com.DOCKin.member.repository.MemberRepository;
import com.DOCKin.safetyCourse.repository.SafetyCourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class SafetyCourseService {
    private final SafetyCourseRepository safetyCourseRepository;
    private final MemberRepository memberRepository;

    //교육 자료 등록
    @Transactional
    public SafetyCourseResponseDto createSafetyCourseResponse(SafetyCourseCreateRequestDto dto, String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        //관리자만 안전교육 생성 가능
        if(member.getRole()!= UserRole.ADMIN){
            throw new BusinessException(ErrorCode.SAFETYCOURSE_AUTHOR);
        }

        SafetyCourse safetyCourse = SafetyCourse.builder().
                title(dto.getTitle()).
                description(dto.getDescription()).
                materialUrl(dto.getMaterialUrl()).
                videoUrl(dto.getVideoUrl()).
                durationMinutes(dto.getDurationMinutes()).
                createdBy(member.getUserId()).build();

        SafetyCourse savedCourse = safetyCourseRepository.save(safetyCourse);

        return SafetyCourseResponseDto.fromEntity(savedCourse);
    }

    //교육 자료 수정
    @Transactional
    public SafetyCourseResponseDto reviseSafetyCourse(SafetyCourseUpdateRequestDto dto, String userId,Integer courseId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        SafetyCourse logs = safetyCourseRepository.findById(courseId)
                .orElseThrow(()->new BusinessException(ErrorCode.SAFETYCOURSE_NOT_FOUND));

        //관리자만 안전교육 수정 가능
        if(member.getRole()!= UserRole.ADMIN){
            throw new BusinessException(ErrorCode.SAFETYCOURSE_AUTHOR);
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

    //전체 교육자료 조회
    @Transactional(readOnly = true)
    public Page<SafetyCourseResponseDto> readSafetyCourse(Pageable pageable){
        Page<SafetyCourse> safetyCourses = safetyCourseRepository.findAll(pageable);
        return safetyCourses.map(SafetyCourseResponseDto::fromEntity);
    }

    //키워드로 교육자료 조회
    @Transactional(readOnly = true)
    public Page<SafetyCourseResponseDto> searchSafetyCourse(String keyword,Pageable pageable){
        Page<SafetyCourse> search = safetyCourseRepository.searchByKeyword(
                keyword,
                pageable
        );

        return search.map(SafetyCourseResponseDto::fromEntity);
    }

    //특정 작성자가 쓴 교육자료 조회
    @Transactional(readOnly = true)
    public Page<SafetyCourseResponseDto> searchOtherSafetyCourse(String targetUserId, Pageable pageable){
       Page<SafetyCourse> safetyCourses = safetyCourseRepository.findByCreatedBy(targetUserId,pageable);
       return safetyCourses.map(SafetyCourseResponseDto::fromEntity);
    }

    //특정 교육자료 삭제
    @Transactional
    public void  deleteSafetyCourse(String userId, Integer courseId){
        SafetyCourse safetyCourse =safetyCourseRepository.findById(courseId)
                .orElseThrow(()->new BusinessException(ErrorCode.SAFETYCOURSE_NOT_FOUND));


        if(!safetyCourse.getCreatedBy().equals(userId)){
            throw new BusinessException(ErrorCode.SAFETYCOURSE_AUTHOR);
        }

        safetyCourseRepository.delete(safetyCourse);
    }

}
