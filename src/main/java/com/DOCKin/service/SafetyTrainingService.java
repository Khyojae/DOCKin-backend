package com.DOCKin.service;

import com.DOCKin.dto.SafetyCourse.CompletedLabel;
import com.DOCKin.dto.SafetyCourse.SafetyCourseResponseDto;
import com.DOCKin.dto.SafetyCourse.SafetyCourseWorkerResponseDto;
import com.DOCKin.dto.SafetyCourse.SafetyWatchStatusRequestDto;
import com.DOCKin.global.error.BusinessException;
import com.DOCKin.global.error.ErrorCode;
import com.DOCKin.model.Member.Member;
import com.DOCKin.model.SafetyCourse.SafetyCourse;
import com.DOCKin.model.SafetyCourse.SafetyEnrollment;
import com.DOCKin.repository.Member.MemberRepository;
import com.DOCKin.repository.SafetyCourse.SafetyCourseRepository;
import com.DOCKin.repository.SafetyCourse.SafetyEnrollmentRepository;
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
        List<SafetyEnrollment> safetyEnrollments= safetyEnrollmentRepository.findAllByUserId_UserId(userId);

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
                findByUserId_UserIdAndCourseId_CourseId(userId,dto.getCourseId())
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
        enrollment.updateStatus(dto.getStatus());
        safetyEnrollmentRepository.save(enrollment);

    }
}
