package com.DOCKin.service;

import com.DOCKin.dto.SafetyCourse.SafetyCourseResponseDto;
import com.DOCKin.repository.SafetyCourse.SafetyCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SafetyTraining {
    private final SafetyCourseRepository safetyCourseRepository;

    //사용자 미이수 영상 조회
    public SafetyCourseResponseDto searchUnwatedVideo(String userId){

    }

    //사용자 영상 조회 완료
}
