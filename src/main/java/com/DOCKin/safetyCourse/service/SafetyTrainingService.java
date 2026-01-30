package com.DOCKin.safetyCourse.service;

import com.DOCKin.safetyCourse.dto.CompletedLabel;
import com.DOCKin.safetyCourse.dto.SafetyCourseWorkerResponseDto;
import com.DOCKin.safetyCourse.dto.SafetyWatchStatusRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.member.model.Member;
import com.DOCKin.safetyCourse.model.SafetyCourse;
import com.DOCKin.safetyCourse.model.SafetyEnrollment;
import com.DOCKin.member.repository.MemberRepository;
import com.DOCKin.safetyCourse.repository.SafetyCourseRepository;
import com.DOCKin.safetyCourse.repository.SafetyEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SafetyTrainingService {
    private final SafetyCourseRepository safetyCourseRepository;
    private final SafetyEnrollmentRepository safetyEnrollmentRepository;
    private final MemberRepository memberRepository;

    //사용자 미이수 영상 조회
    @Transactional(readOnly = true)
    public List<SafetyCourseWorkerResponseDto> searchUnwatchedVideo(String userId){
        List<SafetyCourse> safetyCourses= safetyCourseRepository.findAll();
        List<SafetyEnrollment> safetyEnrollments= safetyEnrollmentRepository.findAllByUserIdUserId(userId);

        return safetyCourses.stream().map(
                course->{
                    SafetyEnrollment enrollment =safetyEnrollments.stream()
                            .filter(e->e.getCourseId().getCourseId().equals(course.getCourseId()))
                            .findFirst()
                            .orElse(null);
                    return SafetyCourseWorkerResponseDto.from(course,enrollment);
                }).filter(dto->dto.getStatus()!= CompletedLabel.WATCHED)
                .collect(Collectors.toList());


    }

    //사용자 영상 조회 완료
    @Transactional
    public void completedViewVideo(String userId,SafetyWatchStatusRequestDto dto){
        SafetyEnrollment enrollment =safetyEnrollmentRepository.
                findByUserIdUserIdAndCourseIdCourseId(userId,dto.getCourseId())
                .orElseGet(()->{
                    Member member =memberRepository.findByUserId(userId)
                            .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));
                    SafetyCourse safetyCourse =safetyCourseRepository.findById(dto.getCourseId())
                            .orElseThrow(()->new BusinessException(ErrorCode.SAFETYCOURSE_NOT_FOUND));

                    return  SafetyEnrollment.builder()
                            .userId(member)
                            .courseId(safetyCourse)
                            .status(CompletedLabel.UNWATCHED)
                            .build();
                });
        enrollment.updateStatus();
        safetyEnrollmentRepository.save(enrollment);

    }
}
